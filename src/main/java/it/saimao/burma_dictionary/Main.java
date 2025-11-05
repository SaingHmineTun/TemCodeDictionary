package it.saimao.burma_dictionary;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;


public class Main extends Application {

    private ListView<Data> lvResult;
    private FlowPane fSynonyms;
    private Label lbSynonym;
    private FlowPane fKeywords;
    private Label lbKeywords;
    private Label lbDefinition; // Replace WebView with Label

    public static void main(String[] args) {

        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        launch(args);
    }

    private final Font titleFont = Font.loadFont(getClass().getResourceAsStream("/fonts/title.ttf"), 26);
    private final Font subTitleFont = Font.loadFont(getClass().getResourceAsStream("/fonts/title.ttf"), 20);
    private final Font footerFont = Font.loadFont(getClass().getResourceAsStream("/fonts/title.ttf"), 12);
    private final Font contentFont = Font.loadFont(getClass().getResourceAsStream("/fonts/content.ttf"), 16);

    List<Data> dataList;

    @Override
    public void start(Stage primaryStage) throws Exception {

        LocalDatabase.ensureDatabaseExists();
        dataList = LocalDatabase.getAllData();

//       File file = new File("data.ser");
//        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
//        out.writeObject(dataList);
//        out.close();

        BorderPane main = new BorderPane();

        main.setLeft(createLeftPane());
        main.setRight(createRightPane());

        Label lbHeader = new Label("Burma Dictionary");
        lbHeader.setFont(titleFont);
        lbHeader.setAlignment(Pos.CENTER);
        lbHeader.setPrefHeight(60);
        lbHeader.setPrefWidth(800);
        BorderPane.setMargin(lbHeader, new Insets(0, 0, 10, 0));
        lbHeader.setPadding(new Insets(5));
        // Create gradient background for header
        lbHeader.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#2C5364")),
                        new Stop(1, Color.web("#0F9BFE"))),
                new CornerRadii(5, 5, 0, 0, false),
                Insets.EMPTY)));
        lbHeader.setTextFill(Color.WHITE);
        lbHeader.setEffect(new DropShadow());
        main.setTop(lbHeader);

        main.setPadding(new Insets(5));
        // Create gradient background for main window
        main.setBackground(new Background(new BackgroundFill(
                new javafx.scene.paint.LinearGradient(0, 0, 1, 1, true,
                        javafx.scene.paint.CycleMethod.NO_CYCLE,
                        new javafx.scene.paint.Stop(0, Color.web("#E0EAFC")),
                        new javafx.scene.paint.Stop(1, Color.web("#CFDEF3"))),
                CornerRadii.EMPTY,
                Insets.EMPTY)));

        Label lbFooter = new Label("Developed by Sai Mao (TMK)");
        lbFooter.setFont(footerFont);
        lbFooter.setTextFill(Color.WHITE);
        BorderPane.setMargin(lbFooter, new Insets(5, 0, 0, 0));
        lbFooter.setAlignment(Pos.CENTER);
        lbFooter.setPrefHeight(30);
        lbFooter.setPrefWidth(800);
        lbFooter.setPadding(new Insets(5));
        //Create gradient background for footer
        lbFooter.setBackground(new Background(new BackgroundFill(
                new javafx.scene.paint.LinearGradient(0, 0, 1, 1, true,
                        javafx.scene.paint.CycleMethod.NO_CYCLE,
                        new javafx.scene.paint.Stop(0, Color.web("#2C5364")),
                        new javafx.scene.paint.Stop(1, Color.web("#0F9BFE"))),
                new CornerRadii(0, 0, 5, 5, false),
                Insets.EMPTY)));
        lbFooter.setEffect(new DropShadow());
        main.setBottom(lbFooter);

        Scene sc = new Scene(main, 820, 600);
        primaryStage.setScene(sc);
        primaryStage.setTitle("Burma Dictionary");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.jpg")));
        primaryStage.setResizable(false);
        primaryStage.show();


        lvResult.getSelectionModel().selectFirst();


    }

    private Label lbWord, lbState;
    private TextField tfSearch;

    private Node createRightPane() {
        VBox main = new VBox();
        main.setPrefWidth(400);
        main.setPadding(new Insets(15));
        main.setSpacing(10);

        // Add rounded corners and border with gradient
        main.setBorder(new Border(new BorderStroke(
                Color.web("#2C5364"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(2))));
        main.setBackground(new Background(new BackgroundFill(
                Color.web("#FFFFFF", 0.8),
                new CornerRadii(10),
                Insets.EMPTY)));

        HBox hbWord = new HBox();
        hbWord.setAlignment(Pos.CENTER_LEFT);
        hbWord.setSpacing(20);
        lbWord = new Label();
        lbWord.setFont(titleFont);
        lbWord.setTextFill(Color.web("#2C5364"));
        ImageView ivPronounce = new ImageView();
        ivPronounce.setCursor(Cursor.HAND);
        ivPronounce.setImage(new Image(getClass().getResourceAsStream("/images/speaker.png")));
        ivPronounce.setPreserveRatio(true);
        ivPronounce.setSmooth(true);
        ivPronounce.setCache(true);
        ivPronounce.setFitWidth(35);
        ivPronounce.setFitHeight(35);
        ivPronounce.setOnMouseClicked(e -> {

            Voice voice = VoiceManager.getInstance().getVoice("kevin16");
            if (voice != null) {
                voice.allocate();
                voice.speak(lbWord.getText());
                voice.deallocate();
            } else {
                System.out.println("Voice not found.");
            }

        });

        hbWord.getChildren().addAll(lbWord, ivPronounce);

        lbState = new Label("");
        lbState.setFont(subTitleFont);
        lbState.setTextFill(Color.web("#0F9BFE"));


        lbDefinition = new Label(); // Use Label instead of WebView
        lbDefinition.setWrapText(true);
        lbDefinition.setFont(contentFont);
        lbDefinition.setTextFill(Color.web("#2C5364"));
        lbDefinition.setPrefHeight(200);
        lbDefinition.setMinHeight(100);
        lbDefinition.setMaxWidth(380);
        lbDefinition.setPadding(new Insets(10));

        lbKeywords = new Label("Keywords");
        lbKeywords.setFont(subTitleFont);
        lbKeywords.setTextFill(Color.web("#2C5364"));

        fKeywords = new FlowPane();
        fKeywords.setPadding(new Insets(10));
        fKeywords.setHgap(8);
        fKeywords.setVgap(8);
        FlowPane.setMargin(lbKeywords, new Insets(20, 0, 0, 0));

        lbSynonym = new Label("Synonyms");
        lbSynonym.setFont(subTitleFont);
        lbSynonym.setTextFill(Color.web("#2C5364"));

        fSynonyms = new FlowPane();
        fSynonyms.setPadding(new Insets(10));
        fSynonyms.setHgap(8);
        fSynonyms.setVgap(8);
        FlowPane.setMargin(fSynonyms, new Insets(20, 0, 0, 0));

        VBox vbScrollable = new VBox(lbDefinition, lbKeywords, fKeywords, lbSynonym, fSynonyms);
        // Make sure the VBox grows to take available space
        VBox.setVgrow(vbScrollable, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(vbScrollable);
        // Configure ScrollPane properly
        scrollPane.setFitToWidth(true);  // Make content fit width
        scrollPane.setFitToHeight(false); // Allow vertical scrolling
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        // Ensure ScrollPane takes available space
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        main.getChildren().addAll(hbWord, lbState, scrollPane);
        // Make main VBox fill available space
        VBox.setVgrow(main, Priority.ALWAYS);

        return main;
    }

    private Node createLeftPane() {
        VBox main = new VBox();
        main.setPrefWidth(400);
        main.setSpacing(10);
        main.setPadding(new Insets(0, 10, 0, 5));

        HBox hbAction = new HBox();
        hbAction.setSpacing(10);
        hbAction.setAlignment(Pos.CENTER_LEFT);
        hbAction.setPadding(new Insets(10));
        hbAction.setBackground(new Background(new BackgroundFill(
                Color.web("#2C5364", 0.9),
                new CornerRadii(8),
                Insets.EMPTY)));
//Search Label
        Label lbSearch = new Label("Search : ");
        lbSearch.setFont(contentFont);
        lbSearch.setTextFill(Color.WHITE);
        // Search TextField
        tfSearch = new TextField();
        tfSearch.setPrefWidth(260);
        tfSearch.setPrefHeight(40);
        tfSearch.setPromptText("Enter word to search...");
        tfSearch.setFont(contentFont);
        tfSearch.setStyle("-fx-border-radius: 5px; -fx-background-radius: 5px;");
        // For Clear Search Button
        Button btClear = new Button();
        ImageView ivClear = new ImageView(new Image(getClass().getResourceAsStream("/images/bin.png")));
        ivClear.setFitWidth(20);
        ivClear.setFitHeight(20);
        btClear.setGraphic(ivClear);
        btClear.setBackground(Background.fill(Paint.valueOf("#FF6B6B")));
        btClear.setTextFill(Color.WHITE);
        btClear.setPrefSize(40, 40);
        btClear.setCursor(Cursor.HAND);
        hbAction.getChildren().addAll(lbSearch, tfSearch, btClear);
        btClear.setFont(contentFont);

        lvResult = new ListView<>();
        lvResult.setPrefHeight(450);
        lvResult.setItems(FXCollections.observableArrayList(dataList));
        lvResult.setCellFactory(new Callback<ListView<Data>, ListCell<Data>>() {
            @Override
            public ListCell<Data> call(ListView<Data> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Data item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Label lbWord = new Label(item.stripWord());
                            lbWord.setFont(contentFont);
                            lbWord.setTextFill(Color.web("#2C5364"));
                            setGraphic(lbWord);
                            setText(null);
                        }
                    }
                };
            }
        });
        lvResult.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null)
                selectWord(newValue);
        }));

// Style the ListView
        lvResult.setBackground(new Background(new BackgroundFill(
                Color.web("#FFFFFF", 0.8),
                new CornerRadii(8),
                Insets.EMPTY)));
        lvResult.setBorder(new Border(new BorderStroke(
                Color.web("#2C5364"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(8),
                new BorderWidths(2))));

        main.getChildren().addAll(hbAction, lvResult);

        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            var data = dataList.stream().filter(da -> da.stripWord().toLowerCase().startsWith(newValue.toLowerCase())).toList();
            lvResult.getItems().clear();
            lvResult.getItems().addAll(data);
            if (lvResult.getItems().isEmpty())
                lvResult.setPlaceholder(createEmptyResultPlaceholder());
            else
                lvResult.getSelectionModel().selectFirst();
        });

        btClear.setOnAction(event -> {
            tfSearch.clear();
        });

        return main;

    }

    private void addKeywords(Data data) {
        if (data.keywords() != null && !data.keywords().isEmpty()) {
            toggleView(lbKeywords, true);
            toggleView(fKeywords, true);
            fKeywords.getChildren().clear();

            for (String keyword : data.keywords().split(",")) {
                Label lbKeyword = new Label(keyword.strip());
                lbKeyword.setCursor(Cursor.HAND);
                lbKeyword.setOnMouseClicked(e -> {
                    tfSearch.setText(keyword.strip());
                    lvResult.getSelectionModel().selectFirst();
                });
                fKeywords.getChildren().add(lbKeyword);
                lbKeyword.setFont(contentFont);
                lbKeyword.setTextFill(Color.web("#2C5364"));
                lbKeyword.setPadding(new Insets(5));
                lbKeyword.setBackground(new Background(new BackgroundFill(
                        Color.web("#FFFFFF", 0.8),
                        new CornerRadii(8),
                        Insets.EMPTY)));
                lbKeyword.setBorder(new Border(new BorderStroke(
                        Color.web("#2C5364"),
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(8),
                        new BorderWidths(2))));

            }

        } else {
            toggleView(lbKeywords, false);
            toggleView(fKeywords, false);

        }
    }

    private void toggleView(Parent p, boolean visible) {
        p.setVisible(visible);
        p.setManaged(visible);
    }

    private void addSynonyms(Data data) {

        if (data.synonyms() != null && !data.synonyms().isEmpty()) {
            toggleView(lbSynonym, true);
            toggleView(fSynonyms, true);
            fSynonyms.getChildren().clear();
            var synonyms = data.synonyms().split(",");
            fSynonyms.getChildren().clear();
            for (String syn : synonyms) {
                Label lbSynonym = new Label(syn.strip());
                lbSynonym.setCursor(Cursor.HAND);
                lbSynonym.setOnMouseClicked(e -> {
                    tfSearch.setText(syn.strip());
                    lvResult.getSelectionModel().selectFirst();
                });
                lbSynonym.setFont(contentFont);
                lbSynonym.setTextFill(Color.web("#0F9BFE"));
                lbSynonym.setPadding(new Insets(5, 10, 5, 10));
                lbSynonym.setBackground(new Background(new BackgroundFill(
                        new javafx.scene.paint.LinearGradient(0, 0, 1, 1, true,
                                javafx.scene.paint.CycleMethod.NO_CYCLE,
                                new javafx.scene.paint.Stop(0, Color.web("#E0EAFC")),
                                new javafx.scene.paint.Stop(1, Color.web("#CFDEF3"))),
                        new CornerRadii(15),
                        Insets.EMPTY)));
                lbSynonym.setBorder(new Border(new BorderStroke(
                        Color.web("#0F9BFE"),
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(15),
                        new BorderWidths(1))));
                fSynonyms.getChildren().add(lbSynonym);
            }
        } else {
            toggleView(lbSynonym, false);
            toggleView(fSynonyms, false);
        }
    }

    private void selectWord(Data newValue) {
        lbWord.setText(newValue.stripWord());
        lbState.setText(newValue.title());
        // Convert HTML to plain text for Label
        String plainText = convertHtmlToPlainText(newValue.definition());
        lbDefinition.setText(plainText);

        // Calculate and set the preferred height based on text content
        lbDefinition.setPrefHeight(Label.USE_COMPUTED_SIZE);
        lbDefinition.autosize();

        addKeywords(newValue);
        addSynonyms(newValue);
    }

    /**
     * Convert basic HTML to plain text
     *
     * @param htmlText HTML text to convert
     * @return Plain text
     */
    private String convertHtmlToPlainText(String htmlText) {
        if (htmlText == null || htmlText.isEmpty()) {
            return "";
        }

        return htmlText
                .replaceAll("<br\\s*/?>", "\n")
                .replaceAll("<p\\s*/?>", "\n")
                .replaceAll("</p>", "\n")
                .replaceAll("<[^>]*>", "") // Remove all other HTML tags
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .trim();
    }

    private Node createEmptyResultPlaceholder() {
        Label lbEmpty = new Label("No results found");
        lbEmpty.setTextFill(Color.web("#2C5364"));
        ImageView ivEmpty = new ImageView();
        ivEmpty.setFitWidth(120);
        ivEmpty.setFitHeight(120);
        ivEmpty.setImage(new Image(getClass().getResourceAsStream("/images/empty.png")));
        ivEmpty.setPreserveRatio(true);
        ivEmpty.setSmooth(true);
        ivEmpty.setCache(true);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(ivEmpty, lbEmpty);
        return vBox;
    }
}