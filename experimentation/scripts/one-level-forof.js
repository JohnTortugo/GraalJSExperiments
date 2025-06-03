function main() {
    const left   = new Array(1_000_000).fill(0);
    forof(left);
}
 
function forof(array) {
    for (let entry of array) {
		if (entry == 1) {
			return true;
		}
    }
    return false;
}
