"use strict";

export const fi_paint_bars = function (bars) {
  return function (ctx) {
    return function () {
      for (var i = 0; i < bars.length; ++i) {
        paintBar(ctx, bars[i]);
      }
    }
  }
};

const paintBar = function (ctx, bar) {
  const yZero = 110.0;
  ctx.strokeStyle = bar.strokeStyle;
  ctx.lineWidth = 0.5;
  const y = bar.yaxis;
  const x = bar.xaxis;
  ctx.beginPath();
  for (var i = 0; i < x.length; ++i) {
    ctx.moveTo(x[i], yZero);
    ctx.lineTo(x[i], y[i]);
  }
  ctx.stroke();
};
