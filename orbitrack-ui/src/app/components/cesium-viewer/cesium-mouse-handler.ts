import { Cartesian2, Entity, ScreenSpaceEventHandler, ScreenSpaceEventType, Viewer } from "cesium";
import { OrbitalBody } from './entites';

export class CesiumMouseHandler {
    private eventHandler: ScreenSpaceEventHandler;
    private bodies: Map<number, OrbitalBody>;
    private viewer: Viewer;
    overBody?: OrbitalBody;
    selectedBody?: OrbitalBody;

    constructor(viewer: Viewer, eventHandler: ScreenSpaceEventHandler, bodies: Map<number, OrbitalBody>) {
        this.viewer = viewer;
        this.eventHandler = eventHandler;
        this.bodies = bodies;
    }

    handleMouseMove(mouseOver: (body: OrbitalBody | undefined, position: Cartesian2) => void,
     mouseOut: (body: OrbitalBody | undefined, position: Cartesian2) => void) : void {
        this.eventHandler.setInputAction((movement: ScreenSpaceEventHandler.MotionEvent) => {
            const position = movement.endPosition;
            const pickedObject = this.viewer.scene.pick(position);
            if (pickedObject) {
                this.overBody = this.getBody(pickedObject);
                mouseOver(this.overBody, position);
            } else {
                mouseOut(this.overBody, position);
            }
        }, ScreenSpaceEventType.MOUSE_MOVE);
    }

    handleMouseClick(mouseClick: (body: OrbitalObjectId | undefined, position: Cartesian2) => void) : void {
        this.eventHandler.setInputAction((movement: ScreenSpaceEventHandler.PositionedEvent) => {
            const position = movement.position;
            const pickedObject = this.viewer.scene.pick(position);
            if (pickedObject) {
                //this.viewer.selectedEntity = undefined;
                const bodyEntry = this.getBodyEntry(pickedObject);
                this.selectedBody = bodyEntry?.body;
                if (this.selectedBody) {
                    this.viewer.selectedEntity = this.selectedBody.cesiumEntity;
                }
                mouseClick(bodyEntry, position);
            }
        }, ScreenSpaceEventType.LEFT_CLICK);
    }

    private getBody(pickedObject: any): OrbitalBody | undefined {
        const bodies = Array.from(this.bodies.values());
        const cesiumEntityId = pickedObject.id._id;
        return bodies.find(body =>
            cesiumEntityId == body.cesiumEntity.id  || cesiumEntityId == body.cesiumTrajectory.id
        );
    }

    private getBodyEntry(pickedObject: any): OrbitalObjectId | undefined {
        var result = undefined;
        const cesiumEntityId = pickedObject.id._id;
        const bodyIds = Array.from(this.bodies.keys());
        bodyIds.forEach(id => {
            const body = this.bodies.get(id);
            if (cesiumEntityId == body?.cesiumEntity.id  || cesiumEntityId == body?.cesiumTrajectory.id) {
                result = {id: id, body: body};
            }
        });
        return result;
    }    
}

export interface OrbitalObjectId {
    id: number;
    body: OrbitalBody;
}