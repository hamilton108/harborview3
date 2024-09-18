module HarborView.UI.Input
  ( InputParams
  , InputVal(..)
  , mkInput
  ) where

import Data.Array ((:))
import DOM.HTML.Indexed.InputType (InputType(..))
import Halogen.HTML (AttrName(..), ClassName(..), HTML)
import Halogen.HTML.Events as HE
import Halogen.HTML.Properties as HP
import Halogen.HTML as HH
import HarborView.UI.Common (Title(..))

import Prelude

data InputVal
  = InpS String
  | InpI Int
  | NoInpS
  | NoInpI
  | InpDate

type InputParams i =
  { title :: Title
  , evt :: String -> i
  , inpVal :: InputVal
  , lblClasses :: Array ClassName
  , inpClasses :: Array ClassName
  , spanClasses :: Array ClassName
  , disabled :: Boolean
  , dateMax :: String
  }

mkInput_ :: forall w i. InputParams i -> HTML w i
mkInput_ p =
  let
    content =
      [ HE.onValueChange p.evt
      , HP.disabled p.disabled
      , HP.classes p.inpClasses
      , HE.onValueChange p.evt
      ]
  in
    case p.inpVal of
      InpS s ->
        HH.input (HP.type_ InputText : HP.value s : content)
      InpI i ->
        HH.input (HP.type_ InputNumber : HP.value (show i) : content)
      NoInpS ->
        HH.input (HP.type_ InputText : content)
      NoInpI ->
        HH.input (HP.type_ InputNumber : content)
      InpDate ->
        HH.input (HP.attr (AttrName "max") p.dateMax : HP.type_ InputDate : content)

mkInput :: forall w i. InputParams i -> HTML w i
mkInput p =
  let
    Title t = p.title
    inp = mkInput_ p
  in
    HH.span [ HP.classes [ ClassName "form-group" ] ]
      [ HH.label [ HP.classes p.lblClasses ] [ HH.text t ]
      , inp
      ]