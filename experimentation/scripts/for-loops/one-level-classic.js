function main() {
    const left = new Array(1_000_000).fill(0);
    classic(left);
}
 
function classic(left) {
    let ll = left.length;
    for (let i=0; i<ll; i++) {
        let leftValue = left[i];
		if (leftValue.valueOf() == 1) {
			return true;
		}
    }
    return false;
}
 