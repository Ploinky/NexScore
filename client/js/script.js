let headers = new Headers()

headers.append('Access-Control-Allow-Origin', '*')


var options = { headers: headers }


fetch("http://ploinky.de:5001", options).then(
  res => {
    res.json().then(
      data => {
        data.forEach(user => {
          
          var table = document.getElementById('scoreTable')
          var row = table.insertRow()
  
          var userCell = row.insertCell()
          userCell.innerHTML = user.username

          var serverCell = row.insertCell()
          serverCell.innerHTML = user.server

          var scoreCell = row.insertCell()
          scoreCell.innerHTML = user.Score

          var totalCell = row.insertCell()
          totalCell.innerHTML = user.Total
        })

        console.log(data)
      }
    )
  }
)