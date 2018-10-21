function loader(config) {
	return function() {
		var radius = Math.min(config.width, config.height) / 2;
	    var tau = 2 * Math.PI;
	    var arc = d3.svg.arc()
	            .innerRadius(radius*0.55)
	            .outerRadius(radius*0.6)
	            .startAngle(0);
	    var svg = d3.select(config.container).append("svg")
	        .attr("id", config.id)
	        .attr("width", config.width)
	        .attr("height", config.height)
	      .append("g")
	        .attr("transform", "translate(" + config.width / 2 + "," + config.height / 2 + ")")
	    var background = svg.append("path")
	            .datum({endAngle: 0.33*tau})
	            .style("fill", "#4D4D4D")
	            .attr("d", arc)
	            .call(spin, 1500)
	    function spin(selection, duration) {
	        selection.transition()
	            .ease("linear")
	            .duration(duration)
	            .attrTween("transform", function() {
	                return d3.interpolateString("rotate(0)", "rotate(360)");
	            });
	        setTimeout(function() { spin(selection, duration); }, duration);
	    }
	    function transitionFunction(path) {
	        path.transition()
	            .duration(7500)
	            .attrTween("stroke-dasharray", tweenDash)
	            .each("end", function() { d3.select(this).call(transition); });
	    }
	  };
}
function visualize() {
	d3.select("svg").remove();
	
	var width = 960,
	    height = 700,
	    radius = Math.min(width, height) / 2;
	
	var waitScreen = loader({width: width, height: height/2 , container: "#loader_container", id: "loader"});
	waitScreen();
	var FromDate = document.getElementById("fromDate").value;
	var ToDate = document.getElementById("toDate").value;
	var authToken = document.getElementById("authToken").value;
	var postData = JSON.stringify({"transactionFromDateTime":FromDate,"transactionToDateTime":ToDate});
	var x = d3.scale.linear()
	    .range([0, 2 * Math.PI]);
	
	var y = d3.scale.linear()
	    .range([0, radius]);
	
	var color = d3.scale.category20c();
	d3.json('/api/drill-down-transactions')
		.header("Content-Type", "application/json")
		.header("Authorization", authToken)
		.post(postData,  function(error, root) {
			d3.select("svg").remove();
		
			var svg = d3.select("body").append("svg")
			    .attr("width", width)
			    .attr("height", height)
			  	.append("g")
			    .attr("transform", "translate(" + width / 2 + "," + (height / 2 + 10) + ")");
	
			var partition = d3.layout.partition()
			    .value(function(d) { return d.amount; });
		
			var arc = d3.svg.arc()
			    .startAngle(function(d) { return Math.max(0, Math.min(2 * Math.PI, x(d.x))); })
			    .endAngle(function(d) { return Math.max(0, Math.min(2 * Math.PI, x(d.x + d.dx))); })
			    .innerRadius(function(d) { return Math.max(0, y(d.y)); })
			    .outerRadius(function(d) { return Math.max(0, y(d.y + d.dy)); });
		    
		  	var g = svg.selectAll("g").data(partition.nodes(root))
		    		.enter().append("g");
		  	var path = g.append("path")
			    .attr("d", arc)
			    .style("fill", function(d) { return color((d.children ? d : d.parent).name); })
			    .on("click", click);
		
		  	var text = g.append("text")
			    .attr("transform", function(d) { return "rotate(" + computeTextRotation(d) + ")"; })
			    .attr("x", function(d) { return y(d.y); })
			    .attr("dx", "6") // margin
			    .attr("dy", ".35em") // vertical-align
			    .text(function(d) {
					return (d.amount) ?  d.name + ' ' + d.currency + ' ' + d.amount : d.name;
			    });
		  	function click(d) {
		    		// fade out all text elements
		    		text.transition().attr("opacity", 0);
		
		    		path.transition()
				      .duration(750)
				      .attrTween("d", arcTween(d))
				      .each("end", function(e, i) {
			          // check if the animated element's data e lies within the visible angle span given in d
			          if (e.x >= d.x && e.x < (d.x + d.dx)) {
			            // get a selection of the associated text element
			            var arcText = d3.select(this.parentNode).select("text");
			            // fade in the text element and recalculate positions
			            arcText.transition().duration(750)
			              .attr("opacity", 1)
			              .attr("transform", function() { return "rotate(" + computeTextRotation(e) + ")" })
			              .attr("x", function(d) { return y(d.y); });
			          }
		      	});
	  		}
  
  			d3.select(self.frameElement).style("height", height + "px");
			//Interpolate the scales!
			function arcTween(d) {
			 	var xd = d3.interpolate(x.domain(), [d.x, d.x + d.dx]),
				    yd = d3.interpolate(y.domain(), [d.y, 1]),
				    yr = d3.interpolate(y.range(), [d.y ? 20 : 0, radius]);
			 	return function(d, i) {
			   		return i ? function(t) { return arc(d); } : function(t) { x.domain(xd(t)); y.domain(yd(t)).range(yr(t)); return arc(d); };
			 	};
			}
	
			function computeTextRotation(d) {
	 			return (x(d.x + d.dx / 2) - Math.PI / 2) / Math.PI * 180;
			}
	});
}