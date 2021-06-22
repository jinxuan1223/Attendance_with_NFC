// set the dimensions and margins of the graph
var margin = {top: 40, right: 40, bottom: 50, left: 70},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// parse the date / time
var parseTime = d3.isoParse;
//var parseTime = d3.timeParse("%d-%b-%y");

// set the ranges
var x = d3.scaleBand().range([0, width]).padding(0.1);
var y = d3.scaleTime().range([height, 0]);

//create var for starting value for y axis 
var start_of_day = new Date();


// append the svg object to the body of the page
// append a 'group' element to 'svg'
// moves the 'group' element to the top left margin
var svg = d3.select('body')
            .append('svg')
            .classed('container', true)
            .append("g")
            .attr("transform","translate(" + margin.left + "," + margin.top + ")");

// get the data
d3.csv("avgLeaveTime.csv").then(function(data) {

    // format the data
    data.forEach(function(d) {

        d.date = parseTime(d.date).toDateString()

        var splitedTime = d.avg_time.split(":");    
        var date = new Date()
        date.setHours(splitedTime[0], splitedTime[1], splitedTime[2],0)
        d.avg_time = date;
        console.log( ' d.avg_time',d.avg_time);
       
        
    });

    //set starting value for y axis
    minHour = d3.min(data,function(d){ return d.avg_time.getHours();})
    start_of_day.setHours(minHour,0,0,0);

    // Scale the range of the data in the domains
    x.domain(data.map(function(d){ return d.date; }));
    y.domain([start_of_day,d3.max(data, function(d){return d.avg_time;})]).nice(d3.max(data, function(d){return d.avg_time}));
    console.log(d3.max(data,function(d){return d.avg_time;}));

    // append the rectangles for the bar chart
    svg.selectAll('.bar')
        .data(data)
        .enter()
        .append('rect')
        .classed('bar',true)
        .attr('width', x.bandwidth())
        .attr('height', function(d){return height - y(start_of_day);})
        .attr('x', function(d){ return x(d.date); })
        .attr('y', function(d){ return y(start_of_day); })

    // apply animation on rectangles
    svg.selectAll("rect")
        .transition()
        .duration(800)
        .attr("y", function(d) { return y(d.avg_time); })
        .attr("height", function(d) { return height - y(d.avg_time); })
        .delay(function(d,i){console.log( 'delay',i) ; return(i*100)})
      

    // add the x Axis 
    svg.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(x))
        .style("color","white")
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
        .call(d3.axisLeft(y))
        .style("color", "white");

    
    // text label for y axis
    svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin.left)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Time")
        .style("fill","white");
    
    // text label for title
    svg.append("text")
        .attr("x", (width / 2))				
        .attr("y", 0 - margin.top/2)
        .attr("text-anchor", "middle")	
        .style("font-size", "24px") 
        .style("text-decoration", "underline")
        .text('Average Leaving Time Of Current Month')
        .style("fill", "white");

})