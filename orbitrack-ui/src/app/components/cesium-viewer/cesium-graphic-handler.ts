import { CallbackProperty, Cartesian3, Color, Entity, Viewer } from "cesium";

export class CesiumGraphicHandler {
    private viewer: Viewer;

    constructor(viewer: Viewer) {
        this.viewer = viewer;
    }

    createPoint(name: string, position: Cartesian3, color: Color, pixelSize: number): Entity {
        return this.viewer.entities.add({
            position: position,
            name: name,
/*            
            billboard: {
                image: "/assets/crosshair.png",
                scale: 0.025
            }
*/                      
            point: {
              pixelSize: pixelSize,
              color: color,
              outlineColor:Color.BLACK,
              outlineWidth: 2,
            }
              
          });
    }

    createPolyline(color: Color, trajectoryPoints: Array<Cartesian3>): Entity {
        return this.viewer.entities.add({
            polyline: {
                positions: new CallbackProperty(() => {
                    return trajectoryPoints;
                }, false),
                width: 1,
                material: color
            }
        });
    }    
}