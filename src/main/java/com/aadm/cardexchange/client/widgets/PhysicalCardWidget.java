package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
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
    HeadingElement cardName;
    @UiField
    DivElement cardStatus;
    @UiField
    DivElement cardDescription;
    @UiField
    Button deleteButton;
    boolean selected = false;
    PhysicalCardWithName pCard;

    public PhysicalCardWidget(PhysicalCardWithName pCard, ImperativeHandlePhysicalCard parent) {
        initWidget(uiBinder.createAndBindUi(this));
        cardContainer.add(new Hyperlink("Open Details",
                "cards/" + pCard.getGameType() + "/" + pCard.getCardId()));
        cardContainer.addDomHandler(e -> {
            setSelected();
            parent.onChangeSelection();
        }, ClickEvent.getType());

        deleteButton.addClickHandler(e -> {
            e.stopPropagation();
            this.removeFromParent();
        });

        cardName.setInnerText(pCard.getName());
        cardStatus.setInnerHTML(pCard.getStatus().name());
        cardDescription.setInnerText(pCard.getDescription());

        this.pCard = pCard;
    }

    public void setSelected() {
        selected = !selected;
        getElement().addClassName(selected ? style.cardSelected() : style.cardDiscarded());
        getElement().removeClassName(selected ? style.cardDiscarded() : style.cardSelected());
    }

    public boolean getSelected() { return selected; }

    public PhysicalCard getPhysicalCard() { return pCard; }

    interface PhysicalCardStyle extends CssResource {
        String cardSelected();
        String cardDiscarded();
    }

    interface PhysicalCardUiBinder extends UiBinder<Widget, PhysicalCardWidget> {
    }
}