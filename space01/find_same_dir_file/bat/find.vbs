Function FilesTree(sPath)    
'����һ���ļ����µ������ļ����ļ���    
    Set oFso = CreateObject("Scripting.FileSystemObject")    
    Set oFolder = oFso.GetFolder(sPath) 
       
    Set oSubFolders = oFolder.SubFolders    '��ǰĿ¼������Ŀ¼        
    Set oFiles = oFolder.Files      '��ǰĿ¼�������ļ�
    
    For Each oFile In oFiles    
        WScript.Echo oFile.Path    
         
    Next    
        
    For Each oSubFolder In oSubFolders    
        WScript.Echo oSubFolder.Path    
 
        FilesTree(oSubFolder.Path)'�ݹ�    
    Next    
        
    Set oFolder = Nothing    
    Set oSubFolders = Nothing    
    Set oFso = Nothing    
End Function    
    
    
'��ȡ��ǰ����·��
FilesTree("F:\log\bat") '����   