package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.utils.DefaultImagePathLookupTable;
import com.aadm.cardexchange.shared.models.Card;
import com.aadm.cardexchange.shared.models.Property;
import com.aadm.cardexchange.shared.models.PropertyType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class CardWidget extends Composite {
    private static final CardUIBinder uiBinder = GWT.create(CardUIBinder.class);
    @UiField
    DivElement nameDiv;
    @UiField
    DivElement categoriesDiv;
    @UiField
    DivElement variantsDiv;
    @UiField
    Image image;
    @UiField
    PushButton detailsButton;

    public CardWidget(ImperativeHandleCard parent, Card card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getProperty("name"));
        image.setPixelSize(90, 131);

        StringBuilder categories = new StringBuilder();
        StringBuilder variants = new StringBuilder();
//        String imageUrl = card.getProperty("image");

        for (Property prop : card.getProperties()) {
            if (prop.getType() == PropertyType.CATEGORICAL)
                categories.append(createDetailHTML(prop.getName(), prop.getValue()));
        }

        for (String variant : card.getVariants()) {
            variants.append("<div>").append(variant).append("</div>");
        }

//        image.setUrl(imageUrl);
        categoriesDiv.setInnerHTML(categories.toString());
        variantsDiv.setInnerHTML(String.valueOf(variants));
        detailsButton.addClickHandler(clickEvent -> parent.onOpenDetailsClick(card.getGameType(), card.getId()));
        image.addErrorHandler((errorEvent) -> image.setUrl(GWT.getHostPageBaseURL() + DefaultImagePathLookupTable.getPath(card.getGameType())));
    }

    private String createDetailHTML(String detail, String text) {
        return "<div>" +
                "<div style=\"font-weight: bold\">" + detail + ":</div>" +
                text +
                "</div>";
    }

    interface CardUIBinder extends UiBinder<Widget, CardWidget> {
    }
}
