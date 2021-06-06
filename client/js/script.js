let headers = new Headers()

headers.append('Access-Control-Allow-Origin', '*')


var options = { headers: headers }


fetch("https://ploinky.de:5001", options).then(
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
          scoreCell.innerHTML = user.score

          var totalCell = row.insertCell()
          totalCell.innerHTML = user.total

          var pctCell = row.insertCell()
          pctCell.innerHTML = user.pct
        })

        console.log(data)
      }
    )
  }
)