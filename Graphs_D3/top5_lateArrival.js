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

// get the data
d3.csv("lateArrival5.csv").then(function(data) {

    // format the data
    data.forEach(function(d) {
        d.Late = +d.Late;
    });

    // Scale the range of the data in the domains
    x.domain(data.map(function(d){ return d.name; }));
    y.domain([0,d3.max(data, function(d){return d.Late;})]);

    // append the rectangles for the bar chart
    svg.selectAll('.bar')
        .data(data)
        .enter()
        .append('rect')
        .classed('bar',true)
        .attr('width', x.bandwidth())
        .attr('height', function(d){return height - y(0);})
        .attr('x', function(d){ return x(d.name); })
        .attr('y', function(d){ return y(0); })

    // apply animation on rectangles
    svg.selectAll("rect")
        .transition()
        .duration(800)
        .attr("y", function(d) { return y(d.Late); })
        .attr("height", function(d) { return height - y(d.Late); })
        .delay(function(d,i){console.log( 'delay',i) ; return(i*100)})
      

    // add the x Axis 
    svg.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(x))
        .style("color","white")
    
    // text label for the x axis
    svg.append("text")
        .attr("x", width / 2 )
        .attr("y", height + margin.top)
        .style("text-anchor", "middle")
        .text("Staff")
        .style("fill", "white");

    // add the y Axis
    svg.append("g")
        .call(d3.axisLeft(y).ticks(d3.max(data, function(d){ return d.Late; })))
        .style("color", "white");

    
    // text label for y axis
    svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left + 20)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Number of Late Arrivals")
        .style("fill","white");
    
    // text label for title
    svg.append("text")
        .attr("x", (width / 2))				
        .attr("y", 0 - margin.top/2)
        .attr("text-anchor", "middle")	
        .style("font-size", "24px") 
        .style("text-decoration", "underline")
        .text('Top 5 Late Arrivals')
        .style("fill", "white");

})
