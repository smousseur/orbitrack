import { Cartesian3, Entity } from "cesium";
import { GeoPosition } from '../../services/geo-position';

const maxTrajectoryPoints = 900;

export class OrbitalBody {
    cesiumEntity: Entity;
    cesiumTrajectory: Entity;
    trajectory: Array<Cartesian3>;
    position: GeoPosition;

    constructor(cesiumEntity: Entity, cesiumTrajectory: Entity, trajectory: Array<Cartesian3>, position: GeoPosition) {
        this.cesiumEntity = cesiumEntity;
        this.cesiumTrajectory = cesiumTrajectory;
        this.trajectory = trajectory;
        this.position = position;
    }

    addTrajectoryPoint(point: Cartesian3) : void {
        this.trajectory.push(point);
        if (this.trajectory.length > maxTrajectoryPoints) {
            this.trajectory.shift();
        }
    }
}