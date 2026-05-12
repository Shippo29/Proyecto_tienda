Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = 'C:\Users\agull\OneDrive\Escritorio\EV2 FullStack\Proyecto_tienda\Pauta Evaluacion 2.xlsx'
$archive = [System.IO.Compression.ZipFile]::OpenRead($zip)
function GetText($n) {
    $e = $archive.GetEntry($n)
    if (-not $e) { return $null }
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
    if ($si.t) {
        $sharedList += $si.t.'#text'
    } else {
        $text = ''
        foreach ($r in $si.r) { $text += $r.t.'#text' }
        $sharedList += $text
    }
}
$wb = [xml](GetText('xl/workbook.xml'))
$rels = [xml](GetText('xl/_rels/workbook.xml.rels'))
foreach ($sheet in $wb.workbook.sheets.sheet) {
    $rId = $sheet.GetAttribute('r:id')
    $rel = $rels.Relationships.Relationship | Where-Object { $_.Id -eq $rId }
    $path = 'xl/' + $rel.Target
    Write-Output "=== SHEET: $($sheet.name) ($path) ==="
    $sheetXml = [xml](GetText($path))
    $ns = New-Object System.Xml.XmlNamespaceManager($sheetXml.NameTable)
    $ns.AddNamespace('x', 'http://schemas.openxmlformats.org/spreadsheetml/2006/main')
    $rows = $sheetXml.worksheet.sheetData.SelectNodes('x:row', $ns)
    if (-not $rows) { Write-Output '(no rows)'; continue }
    $count = 0
    foreach ($row in $rows) {
        $cells = @()
        foreach ($c in $row.SelectNodes('x:c', $ns)) {
            $v = $c.SelectSingleNode('x:v', $ns)
            if ($v) {
                if ($c.GetAttribute('t') -eq 's') {
                    $cells += $sharedList[[int]$v.'#text']
                } else {
                    $cells += $v.'#text'
                }
            } else {
                $cells += ''
            }
        }
        if ($cells -ne @('')) { Write-Output ($cells -join ' | ') }
        $count++
        if ($count -ge 30) { break }
    }
}
$archive.Dispose()
