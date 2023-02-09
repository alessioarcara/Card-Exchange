package com.aadm.cardexchange.client.widgets;

import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardEdit;
import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardRemove;
import com.aadm.cardexchange.client.handlers.ImperativeHandlePhysicalCardSelection;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
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
    HTMLPanel cardStatus;
    @UiField
    DivElement cardDescription;
    @UiField
    HTMLPanel cardActions;
    Button editButton;
    boolean isSelected = false;
    boolean isEditable = false;
    PhysicalCardWithName pCard;

    public PhysicalCardWidget(PhysicalCardWithName pCard, ImperativeHandlePhysicalCardSelection selectionHandler,
                              ImperativeHandlePhysicalCardRemove removeHandler, ImperativeHandlePhysicalCardEdit editHandler) {
        this.pCard = pCard;
        initWidget(uiBinder.createAndBindUi(this));
        cardContainer.add(new Hyperlink("Open Details",
                "cards/" + pCard.getGameType() + "/" + pCard.getCardId()));

        // card selection
        if (selectionHandler != null) {
            cardContainer.addDomHandler(e -> {
                setSelected();
                selectionHandler.onChangeSelection();
            }, ClickEvent.getType());
        }

        // card edit
        if (editHandler != null) {
            editButton = new Button("&#9998", (ClickHandler) e -> {
                e.stopPropagation();
                if (isEditable) {
                    editHandler.onConfirmCardEdit(null, pCard.copyWithModifiedStatusAndDescription(
                            Status.getStatus(Integer.parseInt(((StatusWidget) cardStatus.getWidget(0)).getSelection())),
                            cardDescription.getInnerText()
                    ));
                }
                toggleEditMode();
            });
            editButton.setStyleName(style.editButton());
            cardActions.add(editButton);
        }

        // card remove
        if (removeHandler != null) {
            Button deleteButton = new Button("X", (ClickHandler) e -> {
                e.stopPropagation();
                if (Window.confirm("Are you sure you want to remove this card?")) {
                    removeHandler.onClickDeleteButton(pCard);
                }
            });
            deleteButton.setStyleName(style.deleteButton());
            cardActions.add(deleteButton);
        }

        cardName.setInnerText(pCard.getName());
        setPhysicalCard();
    }

    public PhysicalCardWithName getPhysicalCard() {
        return pCard;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected() {
        if (!isEditable) {
            isSelected = !isSelected;
            if (isSelected) {
                getElement().addClassName(style.cardSelected());
            } else {
                getElement().removeClassName(style.cardSelected());
            }
        }
    }

    private void toggleEditMode() {
        isEditable = !isEditable;
        if (isEditable) {
            getElement().removeClassName(style.cardSelected());
            isSelected = false;
            editButton.addStyleName(style.toggle());
            cardDescription.setAttribute("contenteditable", "true");
            cardStatus.getElement().setInnerText("");
            cardStatus.add(new StatusWidget());
        } else {
            editButton.removeStyleName(style.toggle());
            cardDescription.removeAttribute("contenteditable");
            setPhysicalCard();
        }
    }

    private void setPhysicalCard() {
        cardStatus.getElement().setInnerText(pCard.getStatus().name());
        cardDescription.setInnerText(pCard.getDescription());
    }

    interface PhysicalCardStyle extends CssResource {
        String cardSelected();

        String deleteButton();

        String editButton();

        String toggle();
    }

    interface PhysicalCardUiBinder extends UiBinder<Widget, PhysicalCardWidget> {
    }
}