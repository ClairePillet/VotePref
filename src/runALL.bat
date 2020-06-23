cd /D 
set /A size=3

FOR /L %%a IN (1,1,%size%) DO (
  java -cp "..\..\dist\VoteSMA.jar" VoteSMA.VoteSMA %%a
)
java -cp "..\..\dist\VoteSMA.jar" Chart.Main %size%
PAUSE