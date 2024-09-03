import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class FourierSeriesExample {

    private static final XYSeriesCollection dataset = new XYSeriesCollection();
    private static XYSeries fourierSeries;
    private static XYSeries squareWaveSeries;
    private static final int numPoints = 1000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Senoide e Onda Quadrada");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            squareWaveSeries = createSquareWaveSeries(numPoints);
            dataset.addSeries(squareWaveSeries);
            fourierSeries = createFourierSeries(numPoints, 1);
            dataset.addSeries(fourierSeries);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Aproximação Fourier e Onda Quadrada",
                    "Tempo",
                    "Amplitude",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            XYPlot plot = chart.getXYPlot();

            plot.getRenderer().setSeriesPaint(0, Color.RED);
            plot.getRenderer().setSeriesPaint(1, Color.BLUE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 500));

            JSlider harmonicsSlider = new JSlider(JSlider.HORIZONTAL, 1, 1001, 1);
            harmonicsSlider.setMajorTickSpacing(10);
            harmonicsSlider.setMinorTickSpacing(1);
            harmonicsSlider.setPaintTicks(true);
            harmonicsSlider.setPaintLabels(false);
            harmonicsSlider.addChangeListener(e -> {
                int harmonics = harmonicsSlider.getValue();
                updateFourierSeries(harmonics);
            });

            frame.setLayout(new BorderLayout());
            frame.add(chartPanel, BorderLayout.CENTER);
            frame.add(harmonicsSlider, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        });
    }

    private static XYSeries createFourierSeries(int numPoints, int harmonics) {
        XYSeries series = new XYSeries("Aproximação Fourier (Onda Quadrada)");
        double[] xValues = new double[numPoints];
        double[] yValues = new double[numPoints];
        double step = 4 * Math.PI / numPoints;

        for (int i = 0; i < numPoints; i++) {
            xValues[i] = i * step;
        }

        for (int n = 1; n <= harmonics; n += 2) {
            for (int i = 0; i < numPoints; i++) {
                yValues[i] += (4 / Math.PI) * (1.0 / n) * Math.sin(n * xValues[i]);
            }
        }

        for (int i = 0; i < numPoints; i++) {
            series.add(xValues[i], yValues[i]);
        }

        return series;
    }

    private static XYSeries createSquareWaveSeries(int numPoints) {
        XYSeries series = new XYSeries("Onda Quadrada");
        double step = 4 * Math.PI / numPoints;
        double amplitude = 1.0;

        for (int i = 0; i < numPoints; i++) {
            double x = i * step;
            double y = (i % (numPoints / 2)) < (numPoints / 4) ? amplitude : -amplitude;
            series.add(x, y);
        }

        return series;
    }

    private static void updateFourierSeries(int harmonics) {
        dataset.removeSeries(fourierSeries);
        fourierSeries = createFourierSeries(numPoints, harmonics);
        dataset.addSeries(fourierSeries);
    }
}
