<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Secret Message</title>
</head>
<body>
<h1>Secret Message</h1>
<h3>Encrypt</h3>
<form method="post" action="/inputMessage">
    <textarea name="message" placeholder="Input message"></textarea>
    <br><br>
    <input type="submit">
</form>
<br>
<h3>Decrypt</h3>
<form method="post" action="/inputKey">
    <input type="text" name="key" placeholder="Input key">
    <br><br>
    <textarea name="message" placeholder="Input message"></textarea>
    <br><br>
    <input type="submit">
</form>
<br>
<h3>Output</h3>
<div>
    <input type="text" placeholder="Key output" value="${key}">
    <br><br>
    <textarea placeholder="Message output">${message}</textarea>
</div>
</body>
</html>