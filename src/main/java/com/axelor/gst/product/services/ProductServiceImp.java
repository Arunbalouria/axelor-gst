package com.axelor.gst.product.services;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxLineRepository;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Currency;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.CompanyRepository;
import com.axelor.apps.base.db.repo.CurrencyRepository;
import com.axelor.apps.base.db.repo.PartnerBaseRepository;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.apps.base.service.app.AppBaseService;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.axelor.gst.invoice.service.InvoiceService;
import com.axelor.gst.invoiceline.service.InvoiceLineService;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.inject.Inject;

public class ProductServiceImp implements ProductService {

  @Inject private InvoiceService invoiceService;

  @Inject private InvoiceLineService invoiceLineService;

  @Transactional
  @Override
  public Invoice saveInvoice(List<Integer> productIds) throws Exception {
    Invoice invoice = new Invoice();

    Partner party =
        Beans.get(PartnerBaseRepository.class)
            .all()
            .filter("self.id= :id")
            .bind("id", 14)
            .fetchOne();

    if (party == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR, "Partner Not Created");
    } else {
      invoice.setPartner(party);
    }

    invoice.setStatusSelect(1);
    invoice.setAddress(party.getMainAddress());

    Company company =
        Beans.get(CompanyRepository.class).all().filter("self.id= :id").bind("id", 1).fetchOne();

    if (company == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR, "Company Not Created");
    } else {
      invoice.setCompany(company);
    }

    Currency currency =
        Beans.get(CurrencyRepository.class).all().filter("self.id = :id").bind("id", 46).fetchOne();

    LocalDate ldt = LocalDateTime.now().toLocalDate();
    invoice.setInvoiceDate(ldt);
    invoice.setOperationTypeSelect(1);
    invoice.setOperationSubTypeSelect(1);
    invoice.setCurrency(currency);
    setInvoiceLine(productIds, invoice);
    invoice.setNetAmount(invoiceService.setNetAmount(invoice));
    invoice.setNetCgst(invoiceService.setNetCGST(invoice));
    invoice.setNetIgst(invoiceService.setNetIGST(invoice));
    invoice.setNetSgst(invoiceService.setNetSGST(invoice));
    invoice.setGrossAmount(invoiceService.setGrossAmount(invoice));
    invoice.setPaymentDelay(15);
    invoice.setExTaxTotal(invoice.getCompanyExTaxTotal());
    invoice.setTaxTotal(invoice.getCompanyTaxTotal());
    invoice.setInTaxTotal(invoice.getCompanyInTaxTotal());
    return invoice;
  }

  @Transactional
  private void setInvoiceLine(List<Integer> productIds, Invoice invoice) throws Exception {

    TaxLine taxLine =
        Beans.get(TaxLineRepository.class).all().filter("self.id = :id").bind("id", 1).fetchOne();

    if (taxLine == null) {
      throw new AxelorException(
          TraceBackRepository.CATEGORY_CONFIGURATION_ERROR, "TaxLine Not Created");
    }

    for (Integer productId : productIds) {
      BigDecimal exTaxTotal;
      BigDecimal inTaxTotal;
      Product product =
          Beans.get(ProductRepository.class).all().filter("self.id = ?", productId).fetchOne();

      InvoiceLine invoiceLine = new InvoiceLine();

      invoiceLine.setUnit(product.getUnit());
      invoiceLine.setQty(BigDecimal.ONE);
      invoiceLine.setDiscountAmount(BigDecimal.ZERO);
      invoiceLine.setDiscountTypeSelect(1);
      invoiceLine.setInvoice(invoice);
      invoiceLine.setProduct(product);
      invoiceLine.setPrice(product.getSalePrice());
      invoiceLine.setProductName(product.getCode() + product.getName());
      invoiceLine.setProductCode(product.getCode());
      invoiceLine.setHSBN(product.getHSBN());
      invoiceLine.setTaxLine(taxLine);
      invoiceLine.setGstRate(product.getGstRate());

      exTaxTotal =
          invoiceLine
              .getQty()
              .multiply(invoiceLine.getPrice())
              .setScale(AppBaseService.DEFAULT_NB_DECIMAL_DIGITS, RoundingMode.HALF_UP);

      invoiceLine.setExTaxTotal(exTaxTotal);
      inTaxTotal =
          exTaxTotal.add(
              exTaxTotal.multiply(invoiceLine.getTaxLine().getValue()).divide(new BigDecimal(100)));

      invoiceLine.setInTaxTotal(inTaxTotal);
      invoiceLine.setNetAmount(invoiceLineService.setNetAmount(invoiceLine));
      invoiceLine.setCGST(invoiceLineService.setCgst(invoiceLine, invoice));
      invoiceLine.setIGST(invoiceLineService.setIgst(invoiceLine, invoice));
      invoiceLine.setSGST(invoiceLineService.setSgst(invoiceLine, invoice));
      invoiceLine.setGrossAmount(invoiceLineService.setGrossAmount(invoiceLine));
      invoice.setCompanyExTaxTotal(invoice.getCompanyExTaxTotal().add(exTaxTotal));

      invoice.setCompanyTaxTotal(
          invoice
              .getCompanyTaxTotal()
              .add(
                  exTaxTotal.add(
                      exTaxTotal
                          .multiply(invoiceLine.getTaxLine().getValue())
                          .divide(new BigDecimal(100))))
              .subtract(invoiceLine.getQty().multiply(invoiceLine.getPrice()))
              .setScale(AppBaseService.DEFAULT_NB_DECIMAL_DIGITS, RoundingMode.HALF_UP));

      invoice.setCompanyInTaxTotal(
          invoice
              .getCompanyInTaxTotal()
              .add(inTaxTotal)
              .setScale(AppBaseService.DEFAULT_NB_DECIMAL_DIGITS, RoundingMode.HALF_UP));

      invoice.addInvoiceLineListItem(invoiceLine);
    }
  }
}
