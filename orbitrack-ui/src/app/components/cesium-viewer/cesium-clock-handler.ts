import { Clock, Entity, Viewer, Color, Cartesian3, CallbackProperty, JulianDate } from 'cesium';

export class CesiumClockHandler {
    private clock: Clock;
    private viewer: Viewer;
    isPaused = false;
    private lastTime!: JulianDate;
    private lastMultiplier: number = 1;
    constructor(clock: Clock, viewer: Viewer) {
        this.clock = clock;
        this.viewer = viewer;
    }

    handleClock(multiplierHandler: () => void, pauseHandler: () => void, resumeHandler: () => void): void {
        this.handlePauseResume(pauseHandler, resumeHandler);
        this.handleMultiplier(multiplierHandler);
        this.lastTime = this.clock.currentTime;
    }

    private handleMultiplier(multiplierHandler: () => void): void {
        let timeoutId: any = null;
        if (this.lastTime) {
            const areComparableTime = JulianDate.equalsEpsilon(this.clock.currentTime, this.lastTime, 5 * this.clock.multiplier);
            if (this.clock.multiplier !== this.lastMultiplier || !areComparableTime) {
                timeoutId = setTimeout(() => {
                    if (this.clock.multiplier !== this.lastMultiplier) {
                        multiplierHandler();
                    }
                    this.lastMultiplier = this.clock.multiplier;
                }, 500);                
            }
        }
    }
    private handlePauseResume(pauseHandler: () => void, resumeHandler: () => void): void {
        if (this.lastTime && this.lastTime.equals(this.clock.currentTime)) {
            pauseHandler();
            this.isPaused = true;
        } else {
            if (this.isPaused) {
                resumeHandler();
            }
            this.isPaused = false;
        }
    }
    getIsoClockTime(): string {
        const currentTime = this.clock.currentTime;
        let currentIsoTime: string = JulianDate.toIso8601(currentTime);
        return currentIsoTime.replace(/\.(\d{3})\d*/, '.$1').replace(/Z$/, '');
      }
}
