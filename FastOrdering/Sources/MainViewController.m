//
//  MainViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "MainViewController.h"
#import "NotificationViewController.h"
#import "TablePlanViewController.h"
#import "MenuViewController.h"
#import "AboutViewController.h"
#import "AppDelegate.h"
#import "SocketHelper.h"

@interface MainViewController ()

@end

@implementation MainViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    [self syncDatabase];
    panelShown = NO;
    panelView.hidden = NO;
    overlay.alpha = 0;
    titleLabel.text = NSLocalizedString(@"mainPageTitle", @"");
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Helper methods

- (void)syncDatabase {
    SyncHelper * syncer = [SyncHelper new];
    
    [SocketHelper connectSocket];
    classesToSync = 0;
//
//    [[SocketHelper sharedSocket] put:@"/dish" withData:@{@"name":@"Test", @"available":@YES, @"price":@6.5} callback:^(id response) {
//        DPPLog(@"%@", [response class]);
//        DPPLog(@"%@", response);
//    }];

    NSArray * classes = @[@"DishCategory", @"Dish", @"Order", @"OrderedDish", @"Plan", @"Table"];
    syncer.delegate = self;
    for (NSString * class in classes) {
        classesToSync++;
        [syncer syncClassNamed:class];
    }
    [syncer syncDeletedObjectsOfClasses:classes];
}

- (void)syncEnded {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    NSError * error;
    
    if (![context save:&error])
        PPLog(@"%@", error);
    DLog(@"END SYNC");
    // should hide a loader
}

- (void)loadDatabase {
    // http://stackoverflow.com/questions/10417353/how-to-deal-with-a-multiple-user-database
    DPPLog(@"Should try to load the correct db with coredata");
}

- (void)hidePanel {
    if (panelShown)
        [self showPanel];
}

#pragma mark - IBAction methods

- (IBAction)showPanel {
    panelShown = !panelShown;
    
    [UIView animateWithDuration:0.4 animations:^{
        CGRect frame = panelView.frame;
        frame.origin.x += frame.size.width * (panelShown ? 1 : -1);
        panelView.frame = frame;
        overlay.alpha = panelShown ? 1 : 0;
    }];
}

- (IBAction)buttonClicked:(id)sender {
    [controller.view removeFromSuperview];

    if (sender == mainButton) {
        controller = nil;
        titleLabel.text = NSLocalizedString(@"mainPageTitle", @"");
    } else if (sender == tableButton) {
        controller = [[TablePlanViewController alloc] initWithNibName:@"TablePlanView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"tablePageTitle", @"");
    } else if (sender == menuButton) {
        controller = [[MenuViewController alloc] initWithNibName:@"MenuView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"menuPageTitle", @"");
    } else if (sender == historyButton) {
        controller = nil;
        titleLabel.text = NSLocalizedString(@"historyPageTitle", @"");
    } else if (sender == notificationsButton) {
        controller = [[NotificationViewController alloc] initWithNibName:@"NotificationView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"notificationPageTitle", @"");
    } else if (sender == aboutButton) {
        controller = [[AboutViewController alloc] initWithNibName:@"AboutView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"aboutPageTitle", @"");
    } else {
       DLog(@"UNKNOWN BUTTON %@", sender);
        titleLabel.text = @"";
    }

    [self hidePanel];
    CGRect frame = centralView.frame;
    frame.origin = CGPointZero;
    controller.view.frame = frame;
    [centralView addSubview:controller.view];
}

- (IBAction)logOut {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - ParseSyncer delegate methods

- (void)syncDidFailWithError:(NSError *)error forClass:(NSString *)className {
    PPLog(@"%@ > %@", className, error);
    if (!--classesToSync)
        [self syncEnded];
}

- (void)syncDidStopForClass:(NSString *)className {
    DLog(@"sync OK %@", className);
    if (!--classesToSync)
        [self syncEnded];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
