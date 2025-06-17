function main() {
	const top1   = new Array(1, 2, 3);
	const top2   = new Array(1, 2, 3);
	const top3   = new Array(1, 2, 3);
	
    const left   =getRandomArray(); // new Array(300).fill(0);
    const right  =getRandomArray(); // new Array(300).fill(1);
    const noneof =getRandomArray(); // new Array(300).fill(2);

    return forof(top1, top2, top3, left, right, noneof);
}
 
function forof(top1, top2, top3, left, right, noneof) {
	for (const i of top1) {
		for (const j of top2) {
			for (const k of top3) {
				any(left, right, noneof);
				any(left, right, noneof);
				any(left, right, noneof);
			}
		}
	}
    return false;
}

function any(left, right, noneof) {
	for (const leftValue of left) {
		for (const rightValue of right) {
			if (leftValue.valueOf() === rightValue.valueOf()) {
				return true;
			}
		}
		
		for (const noneValue of noneof) {
			if (leftValue.valueOf() === noneValue.valueOf()) {
				return true;
			}
		}
	}
}

function getRandomArray() {
  const arr = [];
  for (let i = 0; i < 300; i++) {
    // Random values can be numbers, strings, booleans, or null for variety
    const types = [
      () => Math.floor(Math.random() * 100),          // number
      () => Math.random().toString(36).slice(2, 7),   // random string
      () => Math.random() < 0.5                        // boolean
    ];
    const randomType = types[Math.floor(Math.random() * types.length)];
    arr.push(randomType());
  }
  return arr;
}