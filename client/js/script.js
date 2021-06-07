onLoad()

function addUser() {
  dialog = $(
`
    <div id="dialog" title="Create new user">                
        <form>
            <fieldset>
                <label for="username">Username</label>
                <input type="text" name="username" id="username" value="" class="text ui-widget-content ui-corner-all">
                <label for="server">Server</label>
                <input type="text" name="server" id="server" value="" class="text ui-widget-content ui-corner-all">
                <label for="region">Region</label>
                <input type="text" name="region" id="region" value="" class="text ui-widget-content ui-corner-all">
            
                <!-- Allow form submission with keyboard without duplicating the dialog button -->
                <input type="submit" value='Add user' tabindex="-1">
            </fieldset>
        </form>
    </div>`).dialog();

  form = dialog.find( "form" ).on( "submit", function( event ) {
    event.preventDefault();

    var username = dialog.find('#username').val();
    var server = dialog.find('#server').val();
    var region = dialog.find('#region').val();
    
    data = { 'username': username, 'server': server, 'region': region};

    headers = new Headers();
    headers.append('Access-Control-Allow-Origin', '*');
    headers.append('Content-Type', 'application/json');
    var options = { method: 'POST',
    headers: headers,
    body: JSON.stringify(data)}

    console.log(options);
    

    fetch("https://ploinky.de:5001/user", options).then(
      res => {
        alert(res)
        res.json().then(
          data => {alert (data);}
        )
      }
    )

    document.getElementById('username').value = '';
    document.getElementById('server').value = '';
    document.getElementById('region').value = "";
    dialog.dialog( "close" );
  });
}

function toggleDark() {
  
  let root = document.documentElement;

  if(localStorage.getItem('darkmode') == 1)
  {
    root.style.setProperty('--font-color', '#2D3740');
    root.style.setProperty('--primary-color', '#E4E4E4');

    document.getElementById('darkModeButton').innerHTML = 'Dark Mode'
    localStorage.setItem('darkmode', 0)
  }
  else
  {
    root.style.setProperty('--font-color', '#E4E4E4');
    root.style.setProperty('--primary-color', '#2D3740');
    
    localStorage.setItem('darkmode', 1)
    document.getElementById('darkModeButton').innerHTML = 'Light Mode'
  }
}

function onLoad() {
  localStorage.setItem('darkmode', 0);

  let headers = new Headers()

  var options = { headers: headers }

  fetch("https://ploinky.de:5001", options).then(
    res => {
      res.json().then(
        data => {
          data.forEach(user => {
            
            var table = document.getElementById('scoreTable').getElementsByTagName('tbody')[0]
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
}