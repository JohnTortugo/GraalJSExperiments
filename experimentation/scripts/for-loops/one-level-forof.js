function main(items) {
	items[items.length-1].id = -100;
	if (!forof(items)) {
		console.log("Warning. Did not find element!");
	}
}
 
function forof(items) {
    for (let entry of items) {
		if (entry.id == -100) {
			return true;
		}
    }
	return false;
}
