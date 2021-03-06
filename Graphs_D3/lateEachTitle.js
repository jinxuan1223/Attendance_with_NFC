// set the dimensions and margins of the graph
var margin = {top: 40, right: 40, bottom: 50, left: 70},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// The radius of the pieplot is half the width or half the height (smallest one). I subtract a bit of margin.
var radius = Math.min(width, height) / 2 ;

// append the svg object 
var svg = d3.select("body").append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

// set the color scale
var color = d3.scaleOrdinal()
  .range(d3.schemeSet2);

// The arc generator
var arc = d3.arc()
  .innerRadius(0)         
  .outerRadius(radius * 0.8)

// Another arc that won't be drawn. Just for labels positioning
var outerArc = d3.arc()
  .innerRadius(radius * 0.9)
  .outerRadius(radius * 0.9)



// get the data
d3.csv("lateEachTitle.csv").then(function(data) {

    data.forEach(function(d){
        d.numdLate = +d.numLate;
    })  

    color.domain(data);

    // Compute the position of each group on the pie:
    var pie = d3.pie()
        .value(function(d) {return d.numLate; })
    var data_ready = pie(data)
    console.log('data_ready',data_ready);

    // Build the pie chart: Basically, each part of the pie is a path that we build using the arc function.
    svg
    .selectAll('path')
    .data(data_ready)
    .enter()
    .append('path')
    .attr('d', arc)
    .attr('fill', function(d){ return(color(d.data.job_title)) })
    .attr("stroke", "black")
    .style("stroke-width", "2px")

    // Add the polylines between chart and labels:
    svg
    .selectAll('allPolylines')
    .data(data_ready)
    .enter()
    .append('polyline')
    .attr("stroke", "white")
    .style("fill", "none")
    .attr("stroke-width", 1)
    .attr('points', function(d) {
        var posA = arc.centroid(d) // line insertion in the slice
        var posB = outerArc.centroid(d) // line break: we use the other arc generator that has been built only for that
        var posC = outerArc.centroid(d); // Label position = almost the same as posB
        var midangle = d.startAngle + (d.endAngle - d.startAngle) / 2 // we need the angle to see if the X position will be at the extreme right or extreme left
        posC[0] = radius * 0.95 * (midangle < Math.PI ? 1 : -1); // multiply by 1 or -1 to put it on the right or on the left
        return [posA, posB, posC]
    })

    // Add the polylines between chart and labels:
    text = svg.selectAll('text')
            .data(data_ready)
            .enter()
            .append('text')
            .style("fill","white")
            .attr('transform', function(d) {
                var pos = outerArc.centroid(d);
                var midangle = d.startAngle + (d.endAngle - d.startAngle) / 2
                pos[0] = radius * 0.99 * (midangle < Math.PI ? 1 : -1);
                return 'translate(' + pos + ')';
            })
            .style('text-anchor', function(d) {
                var midangle = d.startAngle + (d.endAngle - d.startAngle) / 2
                return (midangle < Math.PI ? 'start' : 'end')
            })
        
    text.append("tspan")
            .text(function(d){ return d.data.job_title;})

    text.append("tspan")
            .text(function(d){ return d.data.numLate;})
            .attr("dy", "1em");
})