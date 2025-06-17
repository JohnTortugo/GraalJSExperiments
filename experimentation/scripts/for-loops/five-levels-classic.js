function main() {
	const top1   = new Array(1, 2, 3);
	const top2   = new Array(1, 2, 3);
	const top3   = new Array(1, 2, 3);
	
    const left   = getRandomArray();
    const right  = getRandomArray();
    const noneof = getRandomArray();

    return classic(top1, top2, top3, left, right, noneof);
}
 
function classic(top1, top2, top3, left, right, noneof) {
	for (let i=0; i<top1.length; i++) {
		for (let j=0; j<top2.length; j++) {
			for (let k=0; k<top3.length; k++) {
				any(left, right, noneof);
				any(left, right, noneof);
				any(left, right, noneof);
			}
		}
	}

    return false;
}

function any(left, right, noneof) {
	for (let l=0; l<left.length; l++) {
		const leftValue = left[l];

		for (let m=0; m<right.length; m++) {
			const rightValue = right[m];
			if (leftValue.valueOf() === rightValue.valueOf()) {
				return true;
			}
		}
		
		for (let m=0; m<noneof.length; m++) {
			const noneValue = noneof[m];
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