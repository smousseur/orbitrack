import { Cartesian3, Entity } from "cesium";

const maxTrajectoryPoints = 200;

export class OrbitalBody {
    cesiumEntity: Entity;
    cesiumTrajectory: Entity;
    trajectory: Array<Cartesian3>;

    constructor(cesiumEntity: Entity, cesiumTrajectory: Entity, trajectory: Array<Cartesian3>) {
        this.cesiumEntity = cesiumEntity;
        this.cesiumTrajectory = cesiumTrajectory;
        this.trajectory = trajectory;
    }

    addTrajectoryPoint(point: Cartesian3) : void {
        this.trajectory.push(point);
        if (this.trajectory.length > maxTrajectoryPoints) {
            this.trajectory.shift();
        }
    }
}