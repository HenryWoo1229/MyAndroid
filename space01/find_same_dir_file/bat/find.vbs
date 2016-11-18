Function FilesTree(sPath)    
'遍历一个文件夹下的所有文件夹文件夹    
    Set oFso = CreateObject("Scripting.FileSystemObject")    
    Set oFolder = oFso.GetFolder(sPath) 
       
    Set oSubFolders = oFolder.SubFolders    '当前目录下所有目录        
    Set oFiles = oFolder.Files      '当前目录下所有文件
    
    For Each oFile In oFiles    
        WScript.Echo oFile.Path    
         
    Next    
        
    For Each oSubFolder In oSubFolders    
        WScript.Echo oSubFolder.Path    
 
        FilesTree(oSubFolder.Path)'递归    
    Next    
        
    Set oFolder = Nothing    
    Set oSubFolders = Nothing    
    Set oFso = Nothing    
End Function    
    
    
'获取当前绝对路径
FilesTree("F:\log\bat") '遍历   