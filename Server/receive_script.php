<?php>

$text = $_POST["text"];
if ($text != null) {
	echo ("Empfangen: ".$text);
	$logfile = fopen("logfile.txt", "a");
	$date = date("d.m.Y H:i:s");
	fwrite($logfile, $date."	Text:".$text."\n");
	fclose($logfile)
} else {
	echo("Keine Datei empfangen.");
}

?>