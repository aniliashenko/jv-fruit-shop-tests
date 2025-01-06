package core.basesyntax;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.*;
import core.basesyntax.service.dao.FileReader;
import core.basesyntax.service.dao.FileReaderImpl;
import core.basesyntax.service.dao.FileWriter;
import core.basesyntax.service.dao.FileWriterImpl;
import core.basesyntax.service.operations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String FILE_PATH = "src/main/resources/reportToRead.csv";
    private static final String REPORT_PATH = "src/main/resources/finalReport.csv";
    private static FileReader fileReader = new FileReaderImpl();

    public static void main(String[] args) {
        List<String> inputReport = fileReader.read(FILE_PATH);

        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);

        DataConverter dataConverter = new DataConverterImpl();
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(inputReport);

        ShopService shopService = new ShopServiceImpl(operationStrategy);
        shopService.process(transactions);

        ReportGenerator reportGenerator = new ReportGeneratorImpl();
        String resultingReport = reportGenerator.getReport(Storage.getFruitTransactionList());

        FileWriter fileWriter = new FileWriterImpl();
        fileWriter.write(resultingReport, REPORT_PATH);
    }
}