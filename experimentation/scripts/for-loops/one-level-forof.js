function main(length) {
    const left   = new Array(length).fill(0);
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
