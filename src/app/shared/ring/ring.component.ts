import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-ring',
  imports: [],
  templateUrl: './ring.component.html',
  styleUrl: './ring.component.css'
})
export class RingComponent implements OnChanges {
  @Input() min: number = 0;
  @Input() max: number = 100;
  @Input() current: number = 0;
  @Input() label: string = '';

  radius = 54;
  circumference = 2 * Math.PI * this.radius;
  progress = 0; // percent

  strokeDashoffset = this.circumference;

  ngOnChanges(changes: SimpleChanges): void {
    this.updateProgress();
  }

  updateProgress() {
    // Clamp current between min and max
    const clamped = Math.min(Math.max(this.current, this.min), this.max);

    // Calculate progress percentage relative to min/max range
    this.progress = ((clamped - this.min) / (this.max - this.min)) * 100;

    // Calculate stroke-dashoffset for SVG circle
    this.strokeDashoffset = this.circumference - (this.progress / 100) * this.circumference;
  }
}
