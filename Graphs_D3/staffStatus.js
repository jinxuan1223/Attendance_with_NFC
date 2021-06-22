d3.csv("staffStatus.csv").then(function(data) {
    // the columns you'd like to display
    var columns = data.columns;

    var table = d3.select("body").append("table"),
        thead = table.append("thead"),
        tbody = table.append("tbody");


    data.forEach(function(d){
    
    });

    // append the header row
    thead.append("tr")
        .selectAll("th")
        .data(columns)
        .enter()
        .append("th")
            .text(function(column) { return column.toUpperCase(); })
            .style("color","white");

    // create a row for each object in the data
    var rows = tbody.selectAll("tr")
        .data(data)
        .enter()
        .append("tr");

    // create a cell in each row for each column
    var cells = rows.selectAll("td")
        .data(function(row) {
            return columns.map(function(column) {
                if (row[column] == 'NULL'){
                    val = '-';
                }else{
                    val = row[column];
                }
                return {column: column, value: val};
            });
        })
        .enter()
        .append("td")
            .style("text-align", 'center')
            .style("color","white")
            .style("background-color", function(d){
                if(d.value == 'Present'){
                    return 'green';
                }else if(d.value == 'Absent'){
                    return 'orange';
                }else if(d.value == 'Left'){
                    return 'red'
                }else{
                    return null;
                }
            })
            .text(function(d) { return d.value.toUpperCase(); });
});