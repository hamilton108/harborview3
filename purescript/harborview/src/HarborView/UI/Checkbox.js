"use strict";

export const randomStr = function() {
  return Math.random().toString(36).slice(2, 7);
}