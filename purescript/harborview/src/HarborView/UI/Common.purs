module HarborView.UI.Common
  ( GridPosition(..)
  , HtmlId(..)
  , Style(..)
  , Title(..)
  ) where

import Prelude

newtype HtmlId =
  HtmlId String

newtype GridPosition =
  GridPosition String

newtype Title =
  Title String

newtype Style =
  Style String

data ModalState
  = ModalHidden
  | ModalInfo String
  | ModalError String

instance Show ModalState where
  show ModalHidden = "ModalHidden"
  show (ModalInfo s) = "ModalInfo " <> s
  show (ModalError s) = "ModalError " <> s
