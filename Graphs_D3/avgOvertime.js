// set the dimensions and margins of the graph
var margin = {top: 40, right: 40, bottom: 50, left: 70},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// parse the date / time
var parseTime = d3.isoParse;
//var parseTime = d3.timeParse("%d-%b-%y");

// set the ranges
var x = d3.scaleOrdinal().range([0, width]);
var y = d3.scaleLinear().range([height, 0]);

//define the line 
var valueline = d3.line()
                .x(function(d){ return x(d.date); })
                .y(function(d){ return y(d.avg_time); });

// append the svg object to the body of the page
// append a 'group' element to 'svg'
// moves the 'group' element to the top left margin
var svg = d3.select('body')
            .append('svg')
            .classed('container', true)
            .append("g")
            .attr("transform","translate(" + margin.left + "," + margin.top + ")");


// get the data
d3.csv("avgOT.csv").then(function(data) {

    var dataset = d3.timeDay
                    .range(d3.min(data, function(d){ return d3.timeParse("%Y-%m-%d")(d.date); }), d3.max(data,function(d){return d3.timeDay.offset(d3.timeParse("%Y-%m-%d")(d.date), 1); }))
                    .map(function(d){ return {date: d, avg_time: 0}})
                    // exclude weekends
	                .filter(d=>![6,0].includes(moment(d.date).day()) )

                // exclude holidays
                .filter(d=>![
                    '2021-04-29',
                    '2021-05-12',
                    '2021-05-13',
                    '2021-05-14',
                ].includes(moment(d.date).format('YYYY-MM-DD')) );

    console.log(dataset);
        
    // format the data
    data.forEach(function(d) {
        d.date = d3.timeParse("%Y-%m-%d")(d.date);
        console.log('ddate',d.date);

        if(d.avg_time < 0){
            d.avg_time = 0;
        }else{
            d.avg_time = +d.avg_time;
        }

        /*for(var i = 0; i < dataset.length; i++){
            if(d.date.toDateString() == dataset[i].date.toDateString() ){
                dataset[i].avg_time = d.avg_time;
                break;
            }
        }*/

    });


    dataset.forEach(d=>{
        let k = data.find(j=> j.date.toDateString() == d.date.toDateString() );
        if (k)  {
            d.avg_time = k.avg_time;
        }
    })

    // Scale the range of the data in the domains
    //x.domain(d3.extent(dataset, function(d) { return d.date; }));
    x.domain(dataset.map(function(d) {return d.date}))
     //.range(dataset.map((d,i)=>width/dataset.length * i));
     .range(dataset.map((d,i)=>width/dataset.length * i));

     console.log('x.domain()', x.domain());
     console.log('x.range()', x.range());

    y.domain(d3.extent(dataset, function(d) { return d.avg_time; })).nice(d3.max(data, function(d){return d.avg_time}));

    //define the line 
    var valueline = d3.line()
    .x(function(d){ return x(d.date); })
    .y(function(d){ return y(d.avg_time); });


    // append the rectangles for the bar chart
    svg.append("path")
        .data([dataset])
        .attr("class", "line")
        .attr("d", valueline);

    // add the x Axis 
    svg.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(x)
            //.ticks(10)
            .tickFormat(d3.timeFormat("%m-%d"))
            //.tickValues(dataset.map(t=>moment(t).date()))
        )
        .style("color","white")
        .attr('transform', 'translate( 0,' +y(0) +')' );
    
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
        .data(data)
        .enter()
        .append("circle")
          .attr("cx", function(d) { return x(d.date) } )
          .attr("cy", function(d) { return y(d.avg_time) } )
          .attr("r", 5)
          .attr("fill", "#69b3a2")

})
