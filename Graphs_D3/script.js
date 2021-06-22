// set the dimensions and margins of the graph
var margin = {top: 20, right: 100, bottom: 50, left: 70},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// parse the date / time
var parseTime = d3.isoParse;

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

var valueline = d3.line()
                .x(function(d){ return d.date; })
                .y(function(d){ return d.total; })

// get the data
d3.csv("attendanceDaily.csv").then(function(data) {

    // format the data
    data.forEach(function(d) {
        d.date = parseTime(d.date).toDateString();
        d.total = +d.total;
    });

    // Scale the range of the data in the domains
    x.domain(data.map(function(d){ return d.date; }));
    y.domain([0,d3.max(data, function(d){return d.total;})]);

    // append the rectangles for the bar chart
    svg.selectAll('.bar')
        .data(data)
        .enter()
        .append('rect')
        .classed('bar',true)
        .style('width', x.bandwidth())
        .style('height', function(d){return height - y(d.total);})
        .attr('x', function(d){ return x(d.date); })
        .attr('y', function(d){ return y(d.total); })

    // add the x Axis 
    svg.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(x))
        .style("color", "white")
        .selectAll("text")	
            .style("text-anchor", "end")
            .style("color", "white")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", "rotate(-10)");
    
    // text label for the x axis
    svg.append("text")
        .attr("x", width / 2 )
        .attr("y", height + margin.top + 20)
        .style("text-anchor", "middle")
        .style("fill","white")
        .text("Date");

    // add the y Axis
    svg.append("g")
        .call(d3.axisLeft(y))
        .style("color","white");

    
    // text label for y axis
    svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left + 20)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .style("fill","white")
        .text("Number of Staffs");
    
    // text label for title
    svg.append("text")
        .attr("x", (width / 2))				
        .attr("y", 0)
        .attr("text-anchor", "middle")	
        .style("font-size", "24px") 
        .style("text-decoration", "underline")
        .style("fill","white")
        .text('Daily Staff Attendance');

    // set label of value
    svg.selectAll('.label')
        .data(data)
        .enter()
        .append('text')
        .text(function(d){ return d.total; })
        .classed('label',true)
        .attr('x', function(d){ return x(d.date) + x.bandwidth()/2; })
        .attr('y', function(d){ return y(d.total) + 15;})
        .attr('text-anchor', 'middle');

})
