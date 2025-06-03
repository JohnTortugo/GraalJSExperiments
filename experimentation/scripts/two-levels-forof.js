function main() {
    const left   = new Array(1_000).fill(0);
    const right  = new Array(1_000).fill(1);
    const noneof = new Array(1_000).fill(2);
    return forof(left, right, noneof);
}
 
function forof(left, right, noneof) {
    for (let leftValue of left) {
        for (let rightValue of right) {
			if (leftValue == rightValue) {
				return true;
			}
        }
		
		for (let noneValue of noneof) {
			if (leftValue == noneValue) {
				return true;
			}
        }
    }
    return false;
}