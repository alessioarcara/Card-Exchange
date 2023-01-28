package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class PhysicalCardWidget extends Composite {
    private static final PhysicalCardUiBinder uiBinder = GWT.create(PhysicalCardUiBinder.class);

    @UiField
    PhysicalCardStyle style;

    @UiField
    HTMLPanel cardContainer;
    @UiField
    Button deleteButton;
    boolean selected = true;
    @UiField
    HeadingElement titleDiv;
    @UiField
    DivElement statusDiv;
    @UiField
    DivElement descriptionDiv;

    public PhysicalCardWidget(Integer physicalCardInteger) {
        initWidget(uiBinder.createAndBindUi(this));
        cardContainer.add(new Hyperlink("Open Details", "cards/yugioh/" + physicalCardInteger));
        cardContainer.addDomHandler(e -> {
            getElement().addClassName(selected ? style.cardSelected() : style.cardDiscarded());
            getElement().removeClassName(selected ? style.cardDiscarded() : style.cardSelected());
            selected = !selected;
        }, ClickEvent.getType());

        deleteButton.addClickHandler(e -> {
            e.stopPropagation();
            this.removeFromParent();
        });

        titleDiv.setInnerHTML(title);
        statusDiv.setInnerHTML(pcard.getStatus().toString());
        descriptionDiv.setInnerHTML(pcard.getDescription());

    }

    interface PhysicalCardStyle extends CssResource {
        String cardSelected();

        String cardDiscarded();
    }

    interface PhysicalCardUiBinder extends UiBinder<Widget, PhysicalCardWidget> {
    }
}