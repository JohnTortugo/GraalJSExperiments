function main(left, right, noneof) {
	left[left.length-1].id = -100;
	noneof[noneof.length-1].id = -100;

	if (!forof(left, right, noneof)) {
		console.log("Warning. Did not find element!");
	}
}
 
function forof(left, right, noneof) {
    for (let leftValue of left) {
        for (let rightValue of right) {
        }
    }
    return false;
}
