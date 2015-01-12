//
//  AboutViewController.h
//  FastOrdering
//
//  Created by Sunder on 05/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AboutViewController : UIViewController {
    IBOutlet UIView *   container;
    IBOutlet UIButton * button;
    IBOutlet UILabel *  versionLabel;
}

- (IBAction)linkClicked;

@end
