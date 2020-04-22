//package net.boardrank.boardgame.ui;
//
//import net.boardrank.backend.service.CompanyService;
//import net.boardrank.backend.service.ContactService;
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.charts.Chart;
//import com.vaadin.flow.component.charts.model.ChartType;
//import com.vaadin.flow.component.charts.model.DataSeries;
//import com.vaadin.flow.component.charts.model.DataSeriesItem;
//import com.vaadin.flow.component.html.Span;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.router.Route;
//
//import java.util.Map;
//
//@Route(value = "dashboard", layout = MainLayout.class)
////@PageTitle("Dashboard | Vaadin CRM")
//public class DashboardView extends VerticalLayout {
//
//    private ContactService contactService;
//    private CompanyService companyService;
//
//    public DashboardView(ContactService contactService, CompanyService companyService) {
//        this.contactService = contactService;
//        this.companyService = companyService;
//        addClassName("dashboard-view");
//        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//
//        add(getContactStats(), getCompaniesChart());
//    }
//
//    private Component getContactStats() {
//        Span stats = new Span(contactService.count() + " contacts");
//        stats.addClassName("contact-stats");
//        return stats;
//    }
//
//    private Chart getCompaniesChart() {
//        Chart chart = new Chart(ChartType.PIE);
//
//        DataSeries dataSeries = new DataSeries();
//        Map<String, Integer> companies = companyService.getStats();
//        companies.forEach((company, employees) ->
//                dataSeries.add(new DataSeriesItem(company, employees)));
//        chart.getConfiguration().setSeries(dataSeries);
//        return chart;
//    }
//}
