# A simple and basic Android App ***To Do List***

Uma aplicação de lista de tarefas simples onde foi implementado:
    
     Um recycleView, um adpter e um viewHolder.
    
     Os dados são persistidos em uma banco de dados SQLite - Utilizado o sqlHelper
     Foi implementado um insert, update, delete e uma pesquisa.
    
     As pesquisa em banco de dados, inserções, deleções e atualizações foram implementadas em threds secundárias para não travar o aplicativo pricipal.
     Acrescentado um layout loader para não parecer travado ao consultar bd, por causa da alta performance do bd provavelmente nunca vai aparecer.
    
    
     Implementação CardView na view Task
