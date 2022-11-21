public class oxmodel {
    private int[][] chess = new int[3][3];
    public int[][] getChess(){
        return chess;
    }
    public boolean judgeTie(){
        boolean flag = true;
        for(int i = 0;i<chess.length;i++){
            for(int j = 0;j<chess[0].length;j++){
                if(chess[i][j] == 2){
                    flag = false;
                }
            }
        }return flag;
    }
    public boolean judgeLine(int who) {
        if(     chess[0][0] ==who && chess[0][1] == who && chess[0][2]==who ||
                chess[1][0] ==who && chess[1][1] == who && chess[1][2]==who ||
                chess[2][0] ==who && chess[2][1] == who && chess[2][2]==who ||
                chess[0][0] ==who && chess[1][0] == who && chess[2][0]==who ||
                chess[0][1] ==who && chess[1][1] == who && chess[2][1]==who ||
                chess[0][2] ==who && chess[1][2] == who && chess[2][2]==who
        )return true;
        else return false;
    }


    public boolean judgeDiagonal(int who) {

        if(chess[0][0] == who && chess[1][1] == who && chess[2][2] == who || chess[0][2] == who && chess[1][1] == who && chess[2][0] == who){
            return true;
        }else{
            return false;
        }
    }



}
