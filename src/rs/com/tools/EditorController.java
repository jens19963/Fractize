package com.rs.tools;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.game.npc.Drop;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Peng on 24.2.2016 22:11.
 */
public class EditorController {

    public TitledPane dropsPane;

    public TableView<DropData> dropsTable;

    public TableColumn<DropData, String> name, amount;
    public TableColumn<DropData, Integer> id;
    public TableColumn<DropData, Double> rate;

    public CheckBox rdtBox;

    public ListView<String> npcList;

    public TextField searchField;

    private ObservableList<String> npcData = FXCollections.observableArrayList();

    private SortedMap<Integer, ArrayList<Drop>> values;

    private NPCDrops npcDrops = new NPCDrops();

    public EditorController() {
        try {
            Cache.init();
            NPCDrops.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        values = new TreeMap<>((Comparator<Integer>) (o1, o2) -> {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return -1;
            }
            return 0;
        });
        values.putAll(npcDrops.getDropArray());
        npcData.addAll(values.keySet().stream().map(id -> NPCDefinitions.getNPCDefinitions(id).getName() + " (ID: " + id + ")").collect(Collectors.toList()));
        searchField.textProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldVal,
                                Object newVal) {
                search((String) oldVal, (String) newVal);
            }
        });

        name.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        id.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(
                new PropertyValueFactory<>("amount"));
        rate.setCellValueFactory(
                new PropertyValueFactory<>("rate"));

        npcList.setItems(npcData);
        dropsTable.setEditable(true);
        amount.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                while (string.split("-").length > 2)
                    string = string.replaceFirst("-", "");
                while (string.indexOf("-") == 0)
                    string = string.replaceFirst("-", "0-");
                while (string.lastIndexOf("-") == string.length() - 1 && !string.equals("-") && !string.equals(""))
                    string = string.substring(0, string.length() - 1);
                if (string.equals("-") || string.equals(""))
                    return "0-0";
                if (!string.contains("-"))
                    string = string + "-" + string;
                return string.replaceAll("[^0-9-]", "");
            }
        }));
        rate.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
            @Override
            public Double fromString(String value) {
                if (value.lastIndexOf(".") == value.length() - 1)
                    value = value.substring(0, value.length() - 2);
                return super.fromString(value.replaceAll("[^0-9.]", ""));
            }

        }));
        id.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            @Override
            public Integer fromString(String value) {
                return super.fromString(value.replaceAll("[^0-9]", ""));
            }
        }));

    }

    public void doClose() {
        System.exit(0);
    }

    public void editId(TableColumn.CellEditEvent<DropData, Integer> e) {
        dropsTable.getItems().get(e.getTablePosition().getRow()).setId(e.getNewValue());
        dropsTable.getItems().get(e.getTablePosition().getRow())
                .setName(ItemDefinitions.getItemDefinitions(e.getNewValue()).getName());
        dropsTable.refresh();
    }

    public void editAmount(TableColumn.CellEditEvent<DropData, String> e) {
        dropsTable.getItems().get(e.getTablePosition().getRow()).setAmount(e.getNewValue());
    }

    public void editRate(TableColumn.CellEditEvent<DropData, Double> e) {
        dropsTable.getItems().get(e.getTablePosition().getRow()).setRate(e.getNewValue());
    }

    public void pack() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            RandomAccessFile raf = new RandomAccessFile("data/npcs/packedDrops.d", "rw");
            raf.writeShort(values.size());
            for (Map.Entry<Integer, ArrayList<Drop>> entry : values.entrySet()) {
                raf.writeShort(entry.getKey());
                raf.writeShort(entry.getValue().size());
                for (Drop d : entry.getValue()) {
                    raf.writeByte(d.isFromRareTable() ? 1 : 0);
                    if (!d.isFromRareTable()) {
                        int itemID = d.getItemId();
                        raf.writeShort(itemID);
                        raf.writeDouble(d.getRate());
                        if (d.getMinAmount() > d.getMaxAmount()) {
                            int min = d.getMinAmount();
                            d.setMinAmount(d.getMaxAmount());
                            d.setMaxAmount(min);
                        }
                        raf.writeInt(d.getMinAmount());
                        raf.writeInt(d.getMaxAmount());
                    }
                }
            }
            raf.close();
            alert.setTitle("Packing successful");
            alert.setHeaderText("Packed the drops successfully");
            alert.setContentText("");
            alert.showAndWait();
        } catch (IOException ex) {
            System.err.println(ex);
            alert.setTitle("Error packing!");
            alert.setHeaderText("Error repacking the drops!");
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    public String searchItems(String prefix) {
        String result = "";
        int count = 0;
        for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
            Item item = new Item(i);
            if (item.getDefinitions().getName().toLowerCase().contains(prefix.toLowerCase())) {
                count++;
                if (count == 50) {
                    return "Too many results";
                }
                String suffix = item.getDefinitions().isNoted() ? "(noted)" : "";
                result += item.getName() + suffix + " id: " + item.getId() + "\n";
            }
        }
        if (result.equals(""))
            result = "No results!";
        return result;
    }

    public void handleButtonAction(ActionEvent e) {
        Button button = (Button) e.getSource();
        switch (button.getText().toLowerCase()) {
            case "add":
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add npc");
                dialog.setHeaderText("Npc id input");
                dialog.setContentText("Please enter npc id:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    try {
                        int id = Integer.parseInt(result.get());
                        npcList.getItems().add(NPCDefinitions.getNPCDefinitions(id).getName() + " (ID: " + id + ")");
                        values.put(id, new ArrayList<>());
                    } catch (NumberFormatException nfe) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Invalid npc id!");
                        alert.setHeaderText("The npc id specified was invalid.");
                        alert.setContentText("");
                        alert.showAndWait();
                    }
                }
                break;
            case "delete":
                String selected = npcList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    npcList.getItems().remove(npcList.getSelectionModel().getSelectedIndex());
                    values.remove(Integer.parseInt(selected.substring(selected.indexOf("(ID: ") + 5,
                            selected.indexOf(")"))));
                }
                break;
            case "find item id":
                TextInputDialog dialog1 = new TextInputDialog();
                dialog1.setTitle("Search items");
                dialog1.setHeaderText("Search for itemid");
                dialog1.setContentText("Enter the itemname or a part of it:");

                Optional<String> result2 = dialog1.showAndWait();
                if (result2.isPresent()) {
                    String resultItems = searchItems(result2.get());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Search results");
                    alert.setHeaderText("");
                    alert.setContentText(resultItems);
                    alert.showAndWait();

                }
                break;
            case "add drop":
                dropsTable.getItems().add(new DropData("null", -1, "0-0", -1));
                dropsTable.refresh();
                break;
            case "delete drop":
                if (dropsTable.getSelectionModel().getSelectedItem() != null) {
                    dropsTable.getItems().remove(dropsTable.getSelectionModel().getSelectedIndex());
                    dropsTable.refresh();
                }
                break;
            case "save":
                String line = npcList.getSelectionModel().getSelectedItem();
                int id = Integer.parseInt(line.substring(line.indexOf("(ID: ") + 5, line.indexOf(")")));
                ArrayList<Drop> drops = values.get(id);
                drops.clear();
                int minAmount;
                int maxAmount;
                String[] amounts;
                for (DropData drop : dropsTable.getItems()) {
                    amounts = drop.getAmount().split("-");
                    minAmount = Integer.parseInt(amounts[0]);
                    maxAmount = Integer.parseInt(amounts[1]);
                    if (minAmount > maxAmount) {
                        int tempAmount = minAmount;
                        minAmount = maxAmount;
                        maxAmount = tempAmount;
                    }
                    drops.add(new Drop(drop.getId(), drop.getRate(), minAmount, maxAmount, false));
                }
                if(rdtBox.isSelected())
                    drops.add(new Drop(-1,-1,1,1,true));
                values.put(id, drops);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Saved!");
                alert.setHeaderText("Npc drops saved!");
                alert.setContentText("Remember to repack before closing program.");
                alert.showAndWait();
                break;
        }
    }

    public void search(String oldVal, String newVal) {
        if (oldVal != null && (newVal.length() < oldVal.length())) {
            npcList.setItems(npcData);
        }
        String value = newVal.toUpperCase();
        ObservableList<String> subentries = FXCollections.observableArrayList();
        for (Object entry : npcList.getItems()) {
            String entryText = (String) entry;
            if (!entryText.toUpperCase().contains(value)) {
                continue;
            } else
                subentries.add(entryText);
        }
        npcList.setItems(subentries);
    }

    public void openNpcData() {
        String line = npcList.getSelectionModel().getSelectedItem();
        int id = Integer.parseInt(line.substring(line.indexOf("(ID: ") + 5, line.indexOf(")")));
        rdtBox.setSelected(false);
        dropsTable.getItems().clear();
        dropsPane.setText("Drops for NPC:" + line);
        ArrayList<Drop> drops = values.get(id);
        for (Drop drop : drops) {
            if (drop.isFromRareTable())
                rdtBox.setSelected(true);
            else
                dropsTable.getItems().add(new DropData(ItemDefinitions.getItemDefinitions(drop.getItemId()).getName(), drop.getItemId(), drop.getMinAmount() + "-" + drop.getMaxAmount(), drop.getRate()));
        }

    }

    public static class DropData {
        SimpleStringProperty name, amount;
        SimpleIntegerProperty id;
        SimpleDoubleProperty rate;

        public DropData(String name, int id, String amount, double rate) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleIntegerProperty(id);
            this.amount = new SimpleStringProperty(amount);
            this.rate = new SimpleDoubleProperty(rate);
        }

        public String getName() {
            return name.get();
        }

        public int getId() {
            return id.get();
        }

        public String getAmount() {
            return amount.get();
        }

        public double getRate() {
            return rate.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public void setAmount(String amount) {
            this.amount.set(amount);
        }

        public void setRate(double rate) {
            this.rate.set(rate);
        }

    }
}
