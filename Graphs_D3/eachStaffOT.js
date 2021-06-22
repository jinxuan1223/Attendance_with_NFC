/*
// set the dimensions and margins of the graph
var margin = {top: 40, right: 40, bottom: 50, left: 70},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// parse the date / time
var parseTime = d3.isoParse;
//var parseTime = d3.timeParse("%d-%b-%y");

// set the ranges
var x = d3.scaleOrdinal();
var y = d3.scaleLinear().range([height, 0]);

//define the line 
var valueline = d3.line()
                .x(function(d){ return x(d.date); })
                .y(function(d){ return y(d.overtime_minutes); });

// append the svg object to the body of the page
// append a 'group' element to 'svg'
// moves the 'group' element to the top left margin
var svg = d3.select('body')
            .append('svg')
            .classed('container', true)
            .append("g")
            .attr("transform","translate(" + margin.left + "," + margin.top + ")");

// get the data
d3.csv("eachStaffOT.csv").then(function(data) {


    // List of groups (here I have one group per column)
    var names = d3.map(data, function(d){return(d.name)});
    var uniqueName = Array.from(new Set(names));
    console.log('uniqueName', uniqueName);

    // format the data
    data.forEach(function(d) {
        d.date = d3.timeParse("%Y-%m-%d")(d.date);

        if(d.overtime_minutes == 'NULL' || d.overtime_minutes < 0){
            d.overtime_minutes = 0;
        }else{
            d.overtime_minutes = +d.overtime_minutes;
        }
    });


    // add the options to the button
    d3.select("body")
        .append('select')
        .selectAll('option')
        .data(uniqueName)
        .enter()
        .append('option')
        .text(function (d) { return d; }) // text showed in the menu
        .attr("value", function (d) { return d; }) // corresponding value returned by the button

    // A color scale: one color for each group
    var myColor = d3.scaleOrdinal()
      .domain(uniqueName)
      .range(d3.schemeSet2);
    


    // Scale the range of the data in the domains
    let currData = data.filter(function(d){return d.name == uniqueName[0];})

    x.domain(d3.extent(currData, function(d) { return d.date; }))
        .range(currData.map((d,i)=>width/currData.length * i));
    y.domain(d3.extent(currData, function(d) { return d.overtime_minutes; })).nice(d3.max(currData, function(d){return d.overtime_minutes}));

    //define the line 
    var valueline = d3.line()
    .x(function(d){ return x(d.date); })
    .y(function(d){ return y(d.overtime_minutes); });
    
    // A function that update the chart
    function update(selectedGroup) {

        // Create new data with the selection?
        var dataFilter = data.filter(function(d){return d.name==selectedGroup})

        x.domain(d3.extent(dataFilter, function(d) { return d.date; }));
        y.domain(d3.extent(dataFilter, function(d) { return d.overtime_minutes; })).nice(d3.max(dataFilter, function(d){return d.overtime_minutes}));


        // Give these new data to update line
        line = svg
            .append('g')
            .append("path")
            .datum(dataFilter)
        
        line.transition()
            .duration(1000)
            .attr("d", valueline)
            .attr("stroke", function(d){ return myColor(selectedGroup) })
            .style("stroke-width", 4)
            .style("fill", "none")

        line.exit()
            .remove()


        // text label for the x axis
        svg.append("text")
            .attr("x", width / 2 )
            .attr("y", height + margin.top)
            .style("text-anchor", "middle")
            .text("Date")
            .style("fill", "white");

        //update x Axis
        svg.select("#xaxis")
            .transition()
            .duration(1000)
            .call(d3.axisBottom(x))
            .style("color", "white");

        // add the y Axis
        svg.select("#yaxis")
            .transition()
            .duration(1000)
            .call(d3.axisLeft(y))
            .style("color", "white");
        }

    // add the x Axis 
    svg.append("g")
        .attr("transform", "translate(0," + height + ")")
        .attr("id", "xaxis")
        .call(d3.axisBottom(x))
        .style("color","white")
        .attr('transform', 'translate( 0,' +y(0) +')' )
        .selectAll("text")	
            .style("text-anchor", "start")
            .style("color","white")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", "rotate(10)");
    
    // text label for the x axis
    svg.append("text")
        .attr("x", width / 2 )
        .attr("y", height + margin.top)
        .style("text-anchor", "middle")
        .text("Date")
        .style("fill", "white");

    // add the y Axis
    svg.append("g")
        .attr("id", "yaxis")
        .call(d3.axisLeft(y))
        .style("color", "white");

    
    // text label for y axis
    svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Minutes")
        .style("fill","white");
    
    // text label for title
    svg.append("text")
        .attr("x", (width / 2))				
        .attr("y", 0 - margin.top/2)
        .attr("text-anchor", "middle")	
        .style("font-size", "24px") 
        .style("text-decoration", "underline")
        .text('Average Over Time Each Day')
        .style("fill", "white");
    
    // dots on the data
    svg .append("g")
        .selectAll("dot")
        .datum(data.filter(function(d){ return d.name == uniqueName[0]}))
        .enter()
        .append("circle")
          .attr("cx", function(d) { return x(d.date) } )
          .attr("cy", function(d) { return y(d.overtime_minutes) } )
          .attr("r", 5)
          .attr("fill", "#69b3a2")
    
    d3.select("select").on("change", function(d) {
        // recover the option that has been chosen
        var selectedOption = d3.select(this).property("value")
        // run the updateChart function with this selected option
        update(selectedOption)
    })

})*/


// set the dimensions and margins of the graph
var margin = {top: 40, right: 40, bottom: 50, left: 70},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;



// set the ranges
var x = d3.scaleBand().range([0, width]).padding(0.1);
var y = d3.scaleLinear().range([height, 0]);

// append the svg object to the body of the page
// append a 'group' element to 'svg'
// moves the 'group' element to the top left margin
var svg = d3.select('body')
            .append('svg')
            .classed('container', true)
            .append("g")
            .attr("transform","translate(" + margin.left + "," + margin.top + ")");

d3.csv("eachStaffOT.csv").then(function(data) {

    // List of groups (here I have one group per column)
    var names = d3.map(data, function(d){return(d.name)});
    var uniqueName = Array.from(new Set(names));

    // format the data
    data.forEach(function(d) {
        d.date = d3.timeParse("%Y-%m-%d")(d.date);
        d.date = d3.timeFormat('%a %b %d')(d.date);
        if(d.overtime_minutes == 'NULL' || d.overtime_minutes< 0){
            d.overtime_minutes = 0;
        }else{
            d.overtime_minutes = +d.overtime_minutes;
        }
    });
    
    // add the options to the button
    d3.select("body")
        .append('select')
        .selectAll('option')
        .data(uniqueName)
        .enter()
        .append('option')
        .text(function (d) { return d; }) // text showed in the menu
        .attr("value", function (d) { return d; }) // corresponding value returned by the button

    // A color scale: one color for each group
    var myColor = d3.scaleOrdinal()
      .domain(uniqueName)
      .range(d3.schemeSet2);

    svg.append("g")
    .attr("id", "xaxis")
    .attr("transform", "translate(0," + height + ")")
    .call(d3.axisBottom(x))
    .selectAll("text")	
        .style("text-anchor", "start")
        .style("color","white")
        .attr("dx", "-.8em")
        .attr("dy", ".15em")
        .attr("transform", "rotate(10)");
    ;
    

    // text label for the x axis
    svg.append("text")
        .attr("x", width / 2 )
        .attr("y", height + margin.top)
        .style("text-anchor", "middle")
        .text("Date")
        .style("fill", "white")


    // add the y Axis
    svg.append("g")
        .attr('id', 'yaxis')
        .call(d3.axisLeft(y))
        .style("color", "white");

    // text label for y axis
    svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Minutes")
        .style("fill","white");
  
    // text label for title
    svg.append("text")
        .attr("x", (width / 2))				
        .attr("y", 0 - margin.top/2)
        .attr("text-anchor", "middle")	
        .style("font-size", "24px") 
        .style("text-decoration", "underline")
        .text('Overtime Each Working Day')
        .style("fill", "white");
      
    // A function that update the chart
    function update(selectedGroup) {

        // Create new data with the selection?
        var dataFilter = data.filter(function(d){return d.name==selectedGroup})

        console.log(dataFilter[5].overtime_minutes)

        x.domain(dataFilter.map(function(d) { return d.date; }));
        y.domain(d3.extent(dataFilter, function(d) { return d.overtime_minutes; }));
    
        // Create the u variable
        var u = svg.selectAll("rect")
            .data(dataFilter)
        
        u
            .enter()
            .append("rect") // Add a new rect for each new elements
            .merge(u) // get the already existing elements as well
            .transition() // and apply changes to all of them
            .duration(1000)
            .attr("x", function(d) { return x(d.date); })
            .attr("y", function(d) { return y(Math.max(0, d.overtime_minutes)); })
            .attr("width", x.bandwidth())
            .attr("height", function(d) { return Math.abs(y(d.overtime_minutes) - y(0)); })
            .attr("fill", function(d){ return myColor(selectedGroup); })

        // If less group in the new dataset, I delete the ones not in use anymore
        u
            .exit()
            .remove();

        svg.select('#xaxis')
            .transition()
            .duration(1000)
            .call(d3.axisBottom(x))
            .style("color","white")
            .attr('transform', 'translate( 0,' +y(0) +')' )

        svg.select('#yaxis')
            .transition()
            .duration(1000)
            .call(d3.axisLeft(y))

    }

    

    update(data[0].name);
    d3.select("select").on("change", function(d) {
        // recover the option that has been chosen
        var selectedOption = d3.select(this).property("value");
        // run the updateChart function with this selected option
        update(selectedOption);
    })
    
})
