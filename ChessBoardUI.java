import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ChessBoardUI {
    private int BOUND = 90;
    private Pane root;
    public Pane getRoot(){
        return root;
    }
    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private oxmodel model = new oxmodel();

    public oxmodel getModel() {
        return model;
    }

    boolean[][] flag = new boolean[3][3];
    public void drawChessboard(){
        root = new Pane();
        root.setPrefSize(270,270);
        line1 = new Line(90,0,90,270);
        line2 = new Line(180,0,180,270);
        line3 = new Line(0,90,270,90);
        line4 = new Line(0,180,270,180);
        root.getChildren().add(line1);
        root.getChildren().add(line2);
        root.getChildren().add(line3);
        root.getChildren().add(line4);
        root.setVisible(true);
    }
    public void drawChess (int i, int j, int color) {
        if(!flag[i][j]){
            if(color == 1){
                drawCircle(i,j);
            }else if(color == 0){
                drawLine(i,j);
            }
        }
    }//更新棋盘中所有值的图像

    private void drawCircle (int i, int j) {//画一个圈
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                        //更新JavaFX的主线程的代码放在此处
                        Circle circle = new Circle();
                        root.getChildren().add(circle);
                        circle.setCenterX(i * BOUND + BOUND / 2.0 );
                        circle.setCenterY(j * BOUND + BOUND / 2.0 );
                        circle.setRadius(BOUND / 2.0 );
                        circle.setStroke(Color.RED);
                        circle.setFill(Color.TRANSPARENT);
                        flag[i][j] = true;


                //更新JavaFX的主线程的代码放在此处
            }
        });

    }

    private void drawLine (int i, int j) {//画一个叉
        Platform.runLater(new Runnable() {
@Override
public void run() {
        Line line_a = new Line();
        Line line_b = new Line();
        root.getChildren().add(line_a);
        root.getChildren().add(line_b);
        line_a.setStartX(i * BOUND);
        line_a.setStartY(j * BOUND);
        line_a.setEndX((i + 1) * BOUND);
        line_a.setEndY((j + 1) * BOUND);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND);
        line_b.setStartY(j * BOUND);
        line_b.setEndX(i * BOUND);
        line_b.setEndY((j + 1) * BOUND);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
}
        });
    }
}
