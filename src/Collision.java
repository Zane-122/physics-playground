public class Collision extends Component {
    @Override
    public void update(PhysicsObject object, Simulation sim) {
        if (object.isStatic()) return;

        for (PhysicsObject other : sim.getObjects()) {
            if (other == object) continue;
            if (object.isParticle() && other.isParticle()) continue;
            if (object.isStatic() && other.isStatic()) continue;

            if (other.isStatic()) {
                tryResolve(object, other, sim);
                continue;
            }

            if (System.identityHashCode(object) > System.identityHashCode(other)) continue;
            tryResolve(object, other, sim);
        }
    }

    private static void tryResolve(PhysicsObject a, PhysicsObject b, Simulation sim) {
        CollisionData data = a.getHitbox().getCollisionData(b.getHitbox());
        if (data == null) return;

        if (a.isParticle() || b.isParticle()) {
            resolveLinear(a, b, data, sim);
        } else {
            resolveAngular(a, b, data, sim);
        }
    }

    private static void resolveLinear(PhysicsObject a, PhysicsObject b, CollisionData data, Simulation sim) {
        double nx = data.normalX();
        double ny = data.normalY();
        double depth = Math.max(data.depth() - Constants.separationSlop, 0);

        boolean staticA = a.isStatic();
        boolean staticB = b.isStatic();

        separate(a, b, nx, ny, depth, staticA, staticB);

        if (staticA || staticB) {
            PhysicsObject dynamic = staticA ? b : a;
            cancelInboundCenterVelocity(dynamic, nx, ny);
            double vRelN = relativeCenterVelocityN(a, b, nx, ny);
            if (vRelN < 0) {
                double restitution = Math.abs(vRelN) < 2.0 * sim.getGravityStep() 
            ? 0.0 
            : Constants.restitution;
                applyCenterImpulse(a, b, nx, ny, staticA, staticB, vRelN, restitution);
            }
            return;
        }

        double vRelN = relativeCenterVelocityN(a, b, nx, ny);
        if (vRelN >= 0) return;
        applyCenterImpulse(a, b, nx, ny, false, false, vRelN, Constants.restitution);
    }

    private static void resolveAngular(PhysicsObject a, PhysicsObject b, CollisionData data, Simulation sim) {
        double nx = data.normalX();
        double ny = data.normalY();
        double depth = Math.max(data.depth() - Constants.separationSlop, 0);
        double cx = data.contactX();
        double cy = data.contactY();

        boolean staticA = a.isStatic();
        boolean staticB = b.isStatic();

        separate(a, b, nx, ny, depth, staticA, staticB);

        if (staticA || staticB) {
            PhysicsObject dynamic = staticA ? b : a;
            double vRelN = relativeContactVelocityN(a, b, nx, ny, cx, cy);
            if (vRelN < 0) {
                double restitution = Math.abs(vRelN) < 2.0 * sim.getGravityStep() 
            ? 0.0 
            : Constants.restitution;
                applyAngularImpulse(a, b, nx, ny, staticA, staticB, cx, cy, vRelN, restitution);
            }
            cancelInboundContactVelocity(dynamic, nx, ny, cx, cy);
            return;
        }

        double vRelN = relativeContactVelocityN(a, b, nx, ny, cx, cy);
        if (vRelN >= 0) return;
        applyAngularImpulse(a, b, nx, ny, false, false, cx, cy, vRelN, Constants.restitution);
    }

    private static double relativeCenterVelocityN(PhysicsObject a, PhysicsObject b, double nx, double ny) {
        return (a.getVelocityX() - b.getVelocityX()) * nx + (a.getVelocityY() - b.getVelocityY()) * ny;
    }

    private static double relativeContactVelocityN(
        PhysicsObject a, PhysicsObject b, double nx, double ny, double cx, double cy
    ) {
        return contactVelocityN(a, nx, ny, cx, cy) - contactVelocityN(b, nx, ny, cx, cy);
    }

    private static double contactVelocityN(PhysicsObject body, double nx, double ny, double cx, double cy) {
        double rx = cx - body.getPosition().x();
        double ry = cy - body.getPosition().y();
        double vx = body.getVelocityX() - body.getAngularVelocity() * ry;
        double vy = body.getVelocityY() + body.getAngularVelocity() * rx;
        return vx * nx + vy * ny;
    }

    private static void applyCenterImpulse(
        PhysicsObject a,
        PhysicsObject b,
        double nx,
        double ny,
        boolean staticA,
        boolean staticB,
        double vRelN,
        double restitution
    ) {
        double invMassA = staticA ? 0 : 1 / a.getMass();
        double invMassB = staticB ? 0 : 1 / b.getMass();
        if (invMassA == 0 && invMassB == 0) return;

        double impulse = -(1 + restitution) * vRelN / (invMassA + invMassB);

        if (!staticA) {
            a.setVelocity(
                a.getVelocityX() + impulse * nx * invMassA,
                a.getVelocityY() + impulse * ny * invMassA
            );
        }
        if (!staticB) {
            b.setVelocity(
                b.getVelocityX() - impulse * nx * invMassB,
                b.getVelocityY() - impulse * ny * invMassB
            );
        }
    }

    private static void applyAngularImpulse(
        PhysicsObject a,
        PhysicsObject b,
        double nx,
        double ny,
        boolean staticA,
        boolean staticB,
        double contactX,
        double contactY,
        double vRelN,
        double restitution
    ) {
        double rAx = contactX - a.getPosition().x();
        double rAy = contactY - a.getPosition().y();
        double rBx = contactX - b.getPosition().x();
        double rBy = contactY - b.getPosition().y();

        double rCrossN_A = rAx * ny - rAy * nx;
        double rCrossN_B = rBx * ny - rBy * nx;

        double invMassA = staticA ? 0 : 1 / a.getMass();
        double invMassB = staticB ? 0 : 1 / b.getMass();
        double invInertiaA = staticA ? 0 : 1 / a.getMomentOfInertia();
        double invInertiaB = staticB ? 0 : 1 / b.getMomentOfInertia();

        double denominator = invMassA + invMassB
            + rCrossN_A * rCrossN_A * invInertiaA
            + rCrossN_B * rCrossN_B * invInertiaB;
        if (denominator == 0) return;

        double impulse = -(1 + restitution) * vRelN / denominator;

        if (!staticA) {
            a.setVelocity(
                a.getVelocityX() + impulse * nx * invMassA,
                a.getVelocityY() + impulse * ny * invMassA
            );
            a.setAngularVelocity(a.getAngularVelocity() + impulse * rCrossN_A * invInertiaA);
        }
        if (!staticB) {
            b.setVelocity(
                b.getVelocityX() - impulse * nx * invMassB,
                b.getVelocityY() - impulse * ny * invMassB
            );
            b.setAngularVelocity(b.getAngularVelocity() - impulse * rCrossN_B * invInertiaB);
        }
    }

    /** Stops center-of-mass motion into a static surface (particles). */
    private static void cancelInboundCenterVelocity(PhysicsObject body, double nx, double ny) {
        double vn = body.getVelocityX() * nx + body.getVelocityY() * ny;
        if (vn < 0) {
            body.setVelocity(body.getVelocityX() - vn * nx, body.getVelocityY() - vn * ny);
        }
    }

    /**
     * Stops inbound velocity at the contact point while preserving tangential spin.
     * Prevents gravity + rotation from re-penetrating static walls each frame.
     */
    private static void cancelInboundContactVelocity(
        PhysicsObject body, double nx, double ny, double cx, double cy
    ) {
        double vn = contactVelocityN(body, nx, ny, cx, cy);
        if (vn >= -0.01) return;

        double rx = cx - body.getPosition().x();
        double ry = cy - body.getPosition().y();
        double rCrossN = rx * ny - ry * nx;
        double invMass = 1 / body.getMass();
        double invInertia = 1 / body.getMomentOfInertia();
        double denominator = invMass + rCrossN * rCrossN * invInertia;
        double impulse = -vn / denominator;

        body.setVelocity(
            body.getVelocityX() + impulse * nx * invMass,
            body.getVelocityY() + impulse * ny * invMass
        );
        body.setAngularVelocity(body.getAngularVelocity() + impulse * rCrossN * invInertia);
    }

    private static void separate(
        PhysicsObject a,
        PhysicsObject b,
        double nx,
        double ny,
        double depth,
        boolean staticA,
        boolean staticB
    ) {
        if (!staticA && !staticB) {
            a.setPosition(new Util.Point(
                a.getPosition().x() + nx * depth * 0.5,
                a.getPosition().y() + ny * depth * 0.5
            ));
            b.setPosition(new Util.Point(
                b.getPosition().x() - nx * depth * 0.5,
                b.getPosition().y() - ny * depth * 0.5
            ));
            a.syncTransform();
            b.syncTransform();
        } else if (!staticA) {
            a.setPosition(new Util.Point(
                a.getPosition().x() + nx * depth,
                a.getPosition().y() + ny * depth
            ));
            a.syncTransform();
        } else if (!staticB) {
            b.setPosition(new Util.Point(
                b.getPosition().x() - nx * depth,
                b.getPosition().y() - ny * depth
            ));
            b.syncTransform();
        }
    }
}
