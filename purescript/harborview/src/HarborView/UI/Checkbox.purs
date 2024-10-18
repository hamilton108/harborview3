module HarborView.UI.Checkbox
  ( CheckboxParam
  , defaultCheckboxParams
  , mkCheckbox
  ) where

import DOM.HTML.Indexed.InputType (InputType(..))
import Halogen.HTML (ClassName(..), HTML)
import Halogen.HTML as HH
import Halogen.HTML.Events as HE
import Halogen.HTML.Properties as HP
import HarborView.UI.Common (Title(..), HtmlId(..))
import Web.HTML.Common (AttrName(..))

foreign import randomStr :: String

type CheckboxParam i =
  { title :: Title
  , htmlId :: HtmlId
  , evt :: Boolean -> i
  , divClz :: Array ClassName
  , labelClz :: Array ClassName
  , inputClz :: Array ClassName
  , isChecked :: Boolean
  }

defaultCheckboxParams :: forall i. Title -> (Boolean -> i) -> CheckboxParam i
defaultCheckboxParams title evt =
  { title: title
  , htmlId: HtmlId randomStr
  , evt: evt
  , divClz: [ ClassName "form-check" ]
  , labelClz: [ ClassName "form-check-label" ]
  , inputClz: [ ClassName "form-check-input" ]
  , isChecked: false
  }

mkCheckbox :: forall w i. CheckboxParam i -> HTML w i
mkCheckbox p =
  let
    HtmlId htmId = p.htmlId
    Title title = p.title
  in
    HH.div [ HP.classes p.divClz ]
      [ HH.input
          [ HP.type_ InputCheckbox
          , HP.classes p.inputClz
          , HE.onChecked p.evt
          , HP.id htmId
          , HP.checked p.isChecked
          ]
      , HH.label
          [ HP.classes p.labelClz
          , HP.attr (AttrName "for") htmId
          ]
          [ HH.text title
          ]
      ]