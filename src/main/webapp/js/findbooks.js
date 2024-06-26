/**
 * convert json array to table.
 * 
 * @param {HTMLElement} results elements to deliver table
 * @param {Object[]} jsonArray response from server 
 */
function toTable(results, jsonArray){
	const table = document.createElement("table");
	results.appendChild(table);
	const th = document.createElement("th");
	table.appendChild(th);
	const header = document.createElement("td");
	table.appendChild(header);
	header.innerText = "path";
	for (let i=0; i<jsonArray.length; i++) {
		const row = document.createElement("tr");
		table.appendChild(row);
		const td = document.createElement("td");
		row.appendChild(td);
		td.innerText = jsonArray[i].path;
	}
}
/**
 * find books on server to write table.
 * 
 * @param {string} inputId id of input element
 * @param {string} resultsId id of result to write table
 */
function submitFunction(inputId, resultsId) {
	const value = document.getElementById(inputId).value;
	const results = document.getElementById(resultsId);
	const uri = "find/books?keyword=" + encodeURIComponent(value);
	async function fetchJson() {
		console.log(`sending:${uri}, value:${value}`);
		const res = await fetch(uri);
		if (!res.ok) {
			console.console.log(`received:${uri}, ${res.ok}`);
			throw new Error(`error status:${res.status}, res.statusText = ${res.statusText}`);
		}
		console.log(`ok status:${res.status}`);
		const jsonArray = await res.json();
	    results.innerHTML="";
		toTable(results, jsonArray);
	}
	fetchJson();
	return false;
}