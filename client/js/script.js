let headers = new Headers()

headers.append('Access-Control-Allow-Origin', '*')


var options = { headers: headers }

fetch("http://ploinky.de:5001", options).then(
  res => {
    res.json().then(
      data => {
        console.log(data.data);
        if (data.data.length > 0) {
            alert(data)
            
        }
      }
    )
  }
)