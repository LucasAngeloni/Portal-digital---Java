document.addEventListener("DOMContentLoaded",
  function (event) {
    
    // Unobtrusive event binding
    document.querySelector("#btnRelevar").addEventListener("click", function () {

    	  var relevancia = parseInt(document.querySelector("#relevancia").textContent);
    	  
    	  console.log(relevancia);
    	  relevancia += 1;
    	  document.querySelector("#relevancia").textContent = relevancia;
    	  
         });
  }
);