Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = 'C:\Users\agull\OneDrive\Escritorio\EV2 FullStack\Proyecto_tienda\Pauta Evaluacion 2.xlsx'
$archive = [System.IO.Compression.ZipFile]::OpenRead($zip)
function GetText($n) {
  $e = $archive.GetEntry($n)
  $s = $e.Open()
  $r = New-Object System.IO.StreamReader($s)
  $t = $r.ReadToEnd()
  $r.Close()
  $s.Close()
  return $t
}
$sharedXml = [xml](GetText('xl/sharedStrings.xml'))
$sharedList = @()
foreach ($si in $sharedXml.sst.si) {
  if ($si.t) { $sharedList += $si.t.'#text' }
  else { $text = ''; foreach ($r in $si.r) { $text += $r.t.'#text' }; $sharedList += $text }
}
$sheetXml = [xml](GetText('xl/worksheets/sheet2.xml'))
$ns = New-Object System.Xml.XmlNamespaceManager($sheetXml.NameTable)
$ns.AddNamespace('x','http://schemas.openxmlformats.org/spreadsheetml/2006/main')
$rows = $sheetXml.worksheet.sheetData.SelectNodes('x:row',$ns)
$i = 0
foreach ($row in $rows) {
  $i++
  $cells = @()
  foreach ($c in $row.SelectNodes('x:c',$ns)) {
    $v = $c.SelectSingleNode('x:v',$ns)
    if ($v) {
      if ($c.GetAttribute('t') -eq 's') { $cells += $sharedList[[int]$v.'#text'] }
      else { $cells += $v.'#text' }
    } else { $cells += '' }
  }
  if ($cells -ne @('')) { Write-Output ($i.ToString() + ': ' + ($cells -join ' | ')) }
}
$archive.Dispose()
